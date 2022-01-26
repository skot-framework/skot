package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.*
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.fileClassBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors


fun Generator.generateIcons() {

    println("icons .........")
    println("@@@@@@@@@ with referenceIconsByVariant = $referenceIconsByVariant")
    println("generate Icons interface .........")
    val drawableDir =
        rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/drawable")
    val drawableXhdpiDir =
        rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/drawable-xhdpi")

    fun Path.listRes(): List<String> = if (!Files.exists(this)) {
        emptyList<String>()
    } else {
        Files.list(this).map { it.fileName.toString().substringBeforeLast(".") }
            .collect(Collectors.toList())
    }

    val icons: List<String> =
        drawableDir.listRes() + drawableXhdpiDir.listRes() +
                if (referenceIconsByVariant) {
                    variantsCombinaison.flatMap {
                        rootPath.resolve(modules.view)
                            .resolve("src/androidMain/res${it}_referenced/drawable-xhdpi")
                            .listRes() +
                                rootPath.resolve(modules.view)
                                    .resolve("src/androidMain/res${it}_referenced/drawable")
                                    .listRes()
                    }
                } else {
                    emptyList()
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
        .writeTo(generatedCommonSources(modules.viewcontract, if (referenceIconsByVariant) mainVariant else null))


    println("generate Icons android implementation .........")
    iconsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(iconsInterface)
        addProperties(
            icons.map {
                PropertySpec.builder(
                    it.toIconsPropertyName(),
                    tech.skot.core.view.Icon::class,
                    KModifier.OVERRIDE
                )
                    .initializer("Icon(R.drawable.$it)")
                    .build()
            }
        )
    }
        .writeTo(generatedAndroidSources(feature ?: modules.app, if (referenceIconsByVariant) mainVariant else null))

    println("generate Icons android for view androidTests .........")
    iconsImpl.fileClassBuilder(listOf(viewR)) {
        addProperties(
            icons.map {
                PropertySpec.builder(
                    it.toIconsPropertyName(),
                    tech.skot.core.view.Icon::class
                )
                    .initializer("Icon(R.drawable.$it)")
                    .build()
            }
        )
    }
        .writeTo(generatedAndroidTestSources(modules.view, if (referenceIconsByVariant) mainVariant else null))


    println("generate Icons mock  .........")
    iconsMock.fileClassBuilder {
        addSuperinterface(iconsInterface)
        addProperties(
            icons.map {
                PropertySpec.builder(
                    it.toIconsPropertyName(),
                    tech.skot.core.view.Icon::class,
                    KModifier.OVERRIDE
                )
                    .initializer("Icon(\"${it.toIconsPropertyName()}\".hashCode())")
                    .build()
            }
        )
    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel, if (referenceIconsByVariant) mainVariant else null))

}