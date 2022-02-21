package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.childElements
import tech.skot.tools.generation.fileClassBuilder
import tech.skot.tools.generation.getDocumentElement
import java.nio.file.Files
import java.util.stream.Collectors
fun String.toAndroidResourcePropertyName() = replace('.','_')

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


    fun String.toColorsPropertyName() = decapitalizeAsciiOnly().replace('.','_')



    FileSpec.builder(
            colorsInterface.packageName,
            colorsInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(colorsInterface.simpleName)
            .addProperties(
                    colors.map {
                        PropertySpec.builder(it.toColorsPropertyName(), tech.skot.core.view.Color::class)
                                .build()
                    }
            )
            .build())
            .build()
            .writeTo(generatedCommonSources(modules.viewcontract))


    println("generate Colors android implementation .........")
    colorsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(colorsInterface)
        addProperties(
                colors.map {
                    PropertySpec.builder(it.toColorsPropertyName(), tech.skot.core.view.Color::class, KModifier.OVERRIDE)
                            .initializer("Color(R.color.${it.toAndroidResourcePropertyName()})")
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(feature ?: modules.app))

    println("generate Colors fot View Android Test .........")
    colorsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(colorsInterface)
        addProperties(
            colors.map {
                PropertySpec.builder(it.toColorsPropertyName(), tech.skot.core.view.Color::class, KModifier.OVERRIDE)
                    .initializer("Color(R.color.${it.toAndroidResourcePropertyName()})")
                    .build()
            }
        )
    }
        .writeTo(generatedAndroidTestSources(modules.view))

    println("generate Colors jvm mock .........")
    colorsMock.fileClassBuilder() {
        addSuperinterface(colorsInterface)
        addProperties(
            colors.map {
                PropertySpec.builder(it.toColorsPropertyName(), tech.skot.core.view.Color::class, KModifier.OVERRIDE)
                    .initializer("Color(\"${it.toColorsPropertyName()}\".hashCode())")
                    .build()
            }
        )
    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))


}