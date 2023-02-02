package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.*
import java.nio.file.Files
import java.util.stream.Collectors


fun Generator.generateStrings() {

    val values = rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/values")



    println("strings .........")

    val regex = Regex("(%[a-zA-Z])")
    fun String.patterns() = regex.findAll(this).map {
        it.groupValues[1]
    }.map { "%" + it }.joinToString(separator = "_")

    val pairsStringsPatterns =
        if (!Files.exists(values)) {
            emptyList()
        } else {

            Files.list(values).flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "string" }
                    .map {
                        Pair(it.getAttribute("name"), it.textContent.patterns())
                    }
            }.collect(Collectors.toList())
        }

    val strings = pairsStringsPatterns.map { it.first }

    fun String.toStringsPropertyName() = decapitalizeAsciiOnly().replace('.', '_')


    FileSpec.builder(
        stringsInterface.packageName,
        stringsInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(stringsInterface.simpleName)
        .addProperties(
            strings.map {
                PropertySpec.builder(it.toStringsPropertyName(), String::class)
                    .build()
            }
        )
        .addFunction(
            FunSpec.builder("get")
                .addParameter("key", String::class)
                .returns(String::class.asTypeName().copy(nullable = true))
                .addModifiers(KModifier.ABSTRACT)
                .build()
        )
        .build())
        .build()
        .writeTo(generatedCommonSources(modules.modelcontract))

//    val funGetSpec = FunSpec.builder("get")
//        .addParameter("key", String::class)
//        .returns(String::class.asTypeName().copy(nullable = true))
//        .addStatement("val id = applicationContext.resources.getIdentifier(key,\"string\",applicationContext.packageName)")
//        .beginControlFlow("return if(id > 0)")
//        .addStatement("get(id)")
//        .endControlFlow()
//        .beginControlFlow("else")
//        .addStatement("null")
//        .endControlFlow()
//        .addModifiers(KModifier.OVERRIDE)
//        .build()

    fun TypeSpec.Builder.stringsImplTypeSpecBuilder(override:Boolean) {
        if (override) {
            addSuperinterface(stringsInterface)
        }
        addPrimaryConstructorWithParams(
            listOf(
                ParamInfos(
                    "applicationContext",
                    AndroidClassNames.context,
                    listOf(KModifier.PRIVATE)
                )
            )
        )
        addFunction(
            FunSpec.builder("get")
                .addModifiers(KModifier.PRIVATE)
                .addParameter("strId", Int::class)
                .returns(String::class)
                .addStatement("return applicationContext.getString(strId)")
                .build()
        )
        addFunction(
            FunSpec.builder("get")
                .addParameter("key", String::class)
                .returns(String::class.asTypeName().copy(nullable = true))
                .addStatement("val id = applicationContext.resources.getIdentifier(key,\"string\",applicationContext.packageName)")
                .beginControlFlow("return if(id > 0)")
                .addStatement("get(id)")
                .endControlFlow()
                .beginControlFlow("else")
                .addStatement("null")
                .endControlFlow()
                .apply {
                    if (override) {
                        addModifiers(KModifier.OVERRIDE)
                    }
                }
                .build()
        )
        addProperties(
            strings.map {
                PropertySpec.builder(it.toStringsPropertyName(), String::class)
                    .apply {
                     if (override) {
                         addModifiers(KModifier.OVERRIDE)
                     }
                    }
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return get(R.string.${it.replace('.', '_')})").build()
                    )
                    .build()
            }
        )
    }
    stringsImpl.fileClassBuilder(listOf(viewR)) {
        stringsImplTypeSpecBuilder(true)
    }.apply {
        writeTo(generatedAndroidSources(feature ?: modules.app))
    }

    stringsImpl.fileClassBuilder(listOf(viewR)) {
        stringsImplTypeSpecBuilder(false)
    }.apply {
        writeTo(generatedAndroidTestSources(modules.view))
    }

    println("generate Strings jvm mock .........")
    stringsMock.fileClassBuilder() {
        addSuperinterface(stringsInterface)
        addProperties(
            pairsStringsPatterns.map {
                PropertySpec.builder(
                    it.first.toStringsPropertyName(),
                    String::class,
                    KModifier.OVERRIDE
                )
                    .initializer("\"${it.first}_${it.second}\"")
                    .build()
            }
        )
        addProperty(
            PropertySpec.builder(name = "getReturnsNull", type = Boolean::class)
                .mutable(true)
                .initializer("false")
                .build()
        )
        addFunction(
            FunSpec.builder("get")
                .addParameter("key", String::class)
                .returns(String::class.asTypeName().copy(nullable = true))
                .addStatement("return if (getReturnsNull) null else key")
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
    }
        .apply {
            writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))
            writeTo(generatedJvmTestSources(modules.model))
        }


}
