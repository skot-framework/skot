package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import java.nio.file.Files
import java.util.stream.Collectors


fun Generator.generateIcons() {

    println("icons .........")
    println("generate Icons interface .........")
    val drawableDir = rootPath.resolve(Modules.view).resolve("src/androidMain/res_referenced/drawable")
    val drawableXhdpiDir = rootPath.resolve(Modules.view).resolve("src/androidMain/res_referenced/drawable-xhdpi")


    val icons:List<String> =
            if (!Files.exists(drawableDir)) {
                emptyList<String>()
            } else {
                Files.list(drawableDir).map { it.fileName.toString().substringBeforeLast(".") }.collect(Collectors.toList())
            } +
                    if (!Files.exists(drawableXhdpiDir)) {
                        emptyList<String>()
                    } else {
                        Files.list(drawableXhdpiDir).map { it.fileName.toString().substringBeforeLast(".") }.collect(Collectors.toList())
                    }


    fun String.toIconsPropertyName() = decapitalize()


    FileSpec.builder(
            iconsInterface.packageName,
            iconsInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(iconsInterface.simpleName)
            .addProperties(
                    icons.map {
                        PropertySpec.builder(it.toIconsPropertyName(), tech.skot.core.view.Icon::class)
                                .build()
                    }
            )
            .build())
            .build()
            .writeTo(generatedCommonSources(Modules.viewcontract))


    println("generate Icons android implementation .........")
    iconsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(iconsInterface)
        addProperties(
                icons.map {
                    PropertySpec.builder(it.toIconsPropertyName(), tech.skot.core.view.Icon::class, KModifier.OVERRIDE)
                            .initializer("Icon(R.drawable.$it)")
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(Modules.app))


}