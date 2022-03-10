package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.core.view.ColorRef
import tech.skot.tools.generation.*
import java.nio.file.Files
import java.util.stream.Collectors

fun String.toAndroidResourcePropertyName() = replace('.', '_')


fun Generator.generateColors() {

    println("colors .........")
    println("generate Colors interface .........")
    val values = rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/values")


    val colors =
        if (!Files.exists(values)) {
            emptyList()
        } else {
            Files.list(values).flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "color" }
                    .map { it.getAttribute("name") }
            }.collect(Collectors.toList())
        }


    fun String.toColorsPropertyName() = decapitalizeAsciiOnly().replace('.', '_')





    FileSpec.builder(
        colorsInterface.packageName,
        colorsInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(colorsInterface.simpleName)
        .addProperties(
            colors.map {
                PropertySpec.builder(
                    it.toColorsPropertyName(),
                    tech.skot.core.view.ColorRef::class
                )
                    .build()
            }
        )
        .addFunction(
            FunSpec.builder("get")
                .addParameter("key", String::class)
                .returns(ColorRef::class.asTypeName().copy(nullable = true))
                .addModifiers(KModifier.ABSTRACT)
                .build()
        )

        .build())
        .build()
        .writeTo(generatedCommonSources(modules.viewcontract))

    val funGetSpec = FunSpec.builder("get")
        .addParameter("key", String::class)
        .returns(ColorRef::class.asTypeName().copy(nullable = true))
        .addStatement("val id = applicationContext.resources.getIdentifier(key,\"color\",applicationContext.packageName)")
        .beginControlFlow("return if(id > 0)")
        .addStatement("ColorRef(id)")
        .endControlFlow()
        .beginControlFlow("else")
        .addStatement("null")
        .endControlFlow()
        .addModifiers(KModifier.OVERRIDE)
        .build()

    println("generate Colors android implementation .........")
    colorsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(colorsInterface)
        addPrimaryConstructorWithParams(
            listOf(
                ParamInfos(
                    "applicationContext",
                    AndroidClassNames.context,
                    listOf(KModifier.PRIVATE)
                )
            )
        )
        addProperties(
            colors.map {
                PropertySpec.builder(
                    it.toColorsPropertyName(),
                    tech.skot.core.view.ColorRef::class,
                    KModifier.OVERRIDE,
                )
                    .initializer("ColorRef(R.color.${it.toAndroidResourcePropertyName()})")
                    .build()
            }
        )
            .addFunction(funGetSpec)
    }
        .writeTo(generatedAndroidSources(feature ?: modules.app))

    println("generate Colors fot View Android Test .........")
    colorsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(colorsInterface)
        addProperties(
            colors.map {
                PropertySpec.builder(
                    it.toColorsPropertyName(),
                    tech.skot.core.view.ColorRef::class,
                    KModifier.OVERRIDE
                )
                    .initializer("ColorRef(R.color.${it.toAndroidResourcePropertyName()})")
                    .build()
            }
        )
            .addFunction(funGetSpec)
    }
        .writeTo(generatedAndroidTestSources(modules.view))

    println("generate Colors jvm mock .........")
    colorsMock.fileClassBuilder() {
        addSuperinterface(colorsInterface)
        addProperties(
            colors.map {
                PropertySpec.builder(
                    it.toColorsPropertyName(),
                    tech.skot.core.view.ColorRef::class,
                    KModifier.OVERRIDE
                )
                    .initializer("ColorRef(\"${it.toColorsPropertyName()}\".hashCode())")
                    .build()
            }
        )
            .addFunction(funGetSpec)

    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))


}