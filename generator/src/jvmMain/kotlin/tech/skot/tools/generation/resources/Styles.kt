package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import java.nio.file.Files
import java.util.stream.Collectors


fun Generator.generateStyles() {

    println("styles .........")
    println("generate Styles interface .........")
    val values = rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/values")


    val colors =
        if (!Files.exists(values)) {
            emptyList()
        } else {
            Files.list(values).flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "style" }
                    .map { it.getAttribute("name") }
            }.collect(Collectors.toList())
        }


    fun String.toStylesPropertyName() = decapitalizeAsciiOnly().replace('.', '_')


    FileSpec.builder(
        stylesInterface.packageName,
        stylesInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(stylesInterface.simpleName)
        .addProperties(
            colors.map {
                PropertySpec.builder(
                    it.toStylesPropertyName(),
                    tech.skot.core.view.Style::class
                )
                    .build()
            }
        )
        .build())
        .build()
        .writeTo(generatedCommonSources(modules.viewcontract))


    println("generate Styles android implementation .........")
    stylesImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(stylesInterface)
        addProperties(
            colors.map {
                PropertySpec.builder(
                    it.toStylesPropertyName(),
                    tech.skot.core.view.Style::class,
                    KModifier.OVERRIDE
                )
                    .initializer("Style(R.style.${it.toAndroidResourcePropertyName()})")
                    .build()
            }
        )
    }
        .writeTo(generatedAndroidSources(feature ?: modules.app))

    println("generate Style jvm mock .........")
    stylesMock.fileClassBuilder() {
        addSuperinterface(stylesInterface)
        addProperties(
            colors.map {
                PropertySpec.builder(it.toStylesPropertyName(), tech.skot.core.view.Style::class, KModifier.OVERRIDE)
                    .initializer("Style(\"${it.toStylesPropertyName()}\".hashCode())")
                    .build()
            }
        )
    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))
}