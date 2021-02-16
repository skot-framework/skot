package tech.skot.generator

import com.squareup.kotlinpoet.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun buildPluralsFile(moduleName: String, withPhrase: Boolean = false) {
    val module = Paths.get("../$moduleName")
    val values = module.resolve("src/androidMain/res/values")
    val modulePackageName = getPackageName(Paths.get("../$moduleName/src/androidMain"))


    val plurals =
            Files.list(values).filter { it.fileName.toString().startsWith("strings") }.flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "plurals" }
                        .map { it.getAttribute("name") }
            }.collect(Collectors.toList())


    //Construction du fichier
    val contextClassName = ClassName("android.content", "Context")
    val phraseClassName = ClassName("com.phrase.android.sdk", "Phrase")


    val fileAndroid = FileSpec.builder("$appPackageName.model", "Plurals").apply {
        if (withPhrase) {
            addImport(phraseClassName.packageName, phraseClassName.simpleName)
        }
    }
    val classBuilderAndroid = TypeSpec.classBuilder("PluralsGen")
            .addSuperinterface(ClassName("$appPackageName.model", "Plurals"))
            .addPrimaryConstructorWithParams(listOf(ParamInfos("applicationContext", contextClassName, listOf(KModifier.PRIVATE))))
            .apply {
                 addProperty(
                    PropertySpec.builder(
                            "applicationContext",
                            contextClassName
                    ).initializer("applicationContext").build()
                )
            }
            .addFunction(
                    FunSpec.builder("compute")
                            .addModifiers(KModifier.PRIVATE)
                            .addParameter("pluralInt", Int::class)
                            .addParameter("quantity", Int::class)
                            .addParameter("formatArgs", Any::class, KModifier.VARARG)
                            .returns(String::class)
                            .beginControlFlow("return if(formatArgs.isEmpty())")
                            .addStatement("applicationContext.resources.getQuantityString(pluralInt, quantity)")
                            .endControlFlow()
                            .beginControlFlow("else")
                            .addStatement("applicationContext.resources.getQuantityString(pluralInt, quantity, *formatArgs)")
                            .endControlFlow()
                            .build()
            )

            .addFunctions(
                    plurals.map {
                        FunSpec.builder(it.decapitalize())
                                .addModifiers(KModifier.OVERRIDE)
                                .addParameter("quantity", Int::class)
                                .addParameter("formatArgs", Any::class, KModifier.VARARG )
                                .returns(String::class)
                                .addCode(CodeBlock.of("return compute(R.plurals.${it}, quantity, *formatArgs)"))
                                .build()
                    }
            )



    fileAndroid.addImport(modulePackageName, "R")
    fileAndroid.addType(classBuilderAndroid.build())

    fileAndroid.build().writeTo(module.resolve("generated/androidMain/kotlin").toFile())


    val fileCommon = FileSpec.builder("$appPackageName.model", "Plurals")
    val classBuilderCommon = TypeSpec.interfaceBuilder("Plurals")
            .addFunctions(
                    plurals.map {
                        FunSpec.builder(it.decapitalize())
                                .addModifiers(KModifier.ABSTRACT)
                                .addParameter("quantity", Int::class)
                                .addParameter("formatArgs", Any::class, KModifier.VARARG)
                                .returns(String::class)
                                .build()
                    }
            )


    fileCommon.addType(classBuilderCommon.build())
    fileCommon.build().writeTo(module.resolve("generated/commonMain/kotlin").toFile())

}
