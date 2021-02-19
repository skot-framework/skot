package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import java.nio.file.Files
import java.util.stream.Collectors


fun Generator.generatePlurals() {

    val values = rootPath.resolve(Modules.view).resolve("src/androidMain/res_referenced/values")


    if (!Files.exists(values)) {
        return
    }
    println("plurals .........")
    val plurals =
            Files.list(values).filter { it.fileName.toString().startsWith("strings") }.flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "plurals" }
                        .map { it.getAttribute("name") }
            }.collect(Collectors.toList())


    fun String.toPluralsFunNAme() = decapitalize()


    FileSpec.builder(
            pluralsInterface.packageName,
            pluralsInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(pluralsInterface.simpleName)
            .addFunctions(
                    plurals.map {
                        FunSpec.builder(it.toPluralsFunNAme())
                                .addModifiers(KModifier.ABSTRACT)
                                .addParameter("quantity", Int::class)
                                .addParameter("formatArgs", Any::class, KModifier.VARARG)
                                .returns(String::class)
                                .build()
                    }
            )
            .build())
            .build()
            .writeTo(generatedCommonSources(Modules.modelcontract))


    pluralsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(pluralsInterface)
        addPrimaryConstructorWithParams(listOf(ParamInfos("applicationContext", AndroidClassNames.context, listOf(KModifier.PRIVATE))))
        addFunction(
                FunSpec.builder("compute")
                        .addModifiers(KModifier.PRIVATE)
                        .addParameter("pluralId", Int::class)
                        .addParameter("quantity", Int::class)
                        .addParameter("formatArgs", Any::class, KModifier.VARARG)
                        .returns(String::class)
                        .beginControlFlow("return if (formatArgs.isEmpty())")
                        .addStatement("applicationContext.resources.getQuantityString(pluralId, quantity)")
                        .endControlFlow()
                        .beginControlFlow("else")
                        .addStatement("applicationContext.resources.getQuantityString(pluralId, quantity, *formatArgs)")
                        .endControlFlow()
                        .build()
        )
        addFunctions(
                plurals.map {
                    FunSpec.builder(it.toPluralsFunNAme())
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("quantity", Int::class)
                            .addParameter("formatArgs", Any::class, KModifier.VARARG)
                            .returns(String::class)
                            .addCode("return compute(R.plurals.$it, quantity, *formatArgs)")
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(Modules.app))



}