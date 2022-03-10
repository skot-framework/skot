package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.*
import tech.skot.core.view.Icon
import tech.skot.tools.generation.*
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
        .addFunction(
            FunSpec.builder("get")
                .addParameter("key", String::class)
                .returns(Icon::class.asTypeName().copy(nullable = true))
                .addModifiers(KModifier.ABSTRACT)
                .build()
        )
        .build())
        .build()
        .writeTo(
            generatedCommonSources(
                modules.viewcontract,
                if (referenceIconsByVariant) mainVariant else null
            )
        )

    val funGetSpec = FunSpec.builder("get")
        .addParameter("key", String::class)
        .returns(Icon::class.asTypeName().copy(nullable = true))
        .addStatement("val id = applicationContext.resources.getIdentifier(key,\"drawable\",applicationContext.packageName)")
        .beginControlFlow("return if(id > 0)")
        .addStatement("Icon(id)")
        .endControlFlow()
        .beginControlFlow("else")
        .addStatement("null")
        .endControlFlow()
        .addModifiers(KModifier.OVERRIDE)
        .build()

    println("generate Icons android implementation .........")
    iconsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(iconsInterface)
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
            .addFunction(funGetSpec)
    }
        .writeTo(
            generatedAndroidSources(
                feature ?: modules.app,
                if (referenceIconsByVariant) mainVariant else null
            )
        )

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
            .addFunction(funGetSpec)
    }
        .writeTo(
            generatedAndroidTestSources(
                modules.view,
                if (referenceIconsByVariant) mainVariant else null
            )
        )


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
            .addFunction(funGetSpec)

    }
        .writeTo(
            generatedJvmTestSources(
                feature ?: modules.viewmodel,
                if (referenceIconsByVariant) mainVariant else null
            )
        )

}