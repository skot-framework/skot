package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import java.nio.file.Files
import java.util.stream.Collectors


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


    fun String.toColorsPropertyName() = decapitalize()


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
                            .initializer("Color(R.color.$it)")
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(feature ?: modules.app))


}