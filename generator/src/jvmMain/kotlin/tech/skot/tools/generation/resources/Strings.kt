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
    }.map { "%"+it }.joinToString(separator = " ")

    val pairsStringsPatterns =
            if (!Files.exists(values)) {
                emptyList()
            } else {

                Files.list(values).flatMap {
                    it.getDocumentElement().childElements().stream().filter { it.tagName == "string" }
                            .map {
                                Pair(it.getAttribute("name"),it.textContent.patterns()) }
                }.collect(Collectors.toList())
            }

    val strings = pairsStringsPatterns.map { it.first }

    fun String.toStringsPropertyName() = decapitalizeAsciiOnly().replace('.','_')


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
            .build())
            .build()
            .writeTo(generatedCommonSources(modules.modelcontract))


    stringsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(stringsInterface)
        addPrimaryConstructorWithParams(listOf(ParamInfos("applicationContext", AndroidClassNames.context, listOf(KModifier.PRIVATE))))
        addFunction(
                FunSpec.builder("get")
                        .addModifiers(KModifier.PRIVATE)
                        .addParameter("strId", Int::class)
                        .returns(String::class)
                        .addStatement("return applicationContext.getString(strId)")
                        .build()
        )
        addProperties(
                strings.map {
                    PropertySpec.builder(it.toStringsPropertyName(), String::class, KModifier.OVERRIDE)
                            .getter(FunSpec.getterBuilder().addStatement("return get(R.string.${it.replace('.', '_')})").build())
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(feature ?: modules.app))

    println("generate Strings jvm mock .........")
    stringsMock.fileClassBuilder() {
        addSuperinterface(stringsInterface)
        addProperties(
            pairsStringsPatterns.map {
                PropertySpec.builder(it.first.toStringsPropertyName(), String::class, KModifier.OVERRIDE)
                    .initializer("\"${it.first}_${it.second}\"")
                    .build()
            }
        )
    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))

}