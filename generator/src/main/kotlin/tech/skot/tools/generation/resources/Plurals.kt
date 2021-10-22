package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.*
import java.nio.file.Files
import java.util.stream.Collectors


fun Generator.generatePlurals() {

    val values = rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/values")



    println("plurals .........")
    val plurals =
            if (!Files.exists(values)) {
                emptyList()
            } else {
                Files.list(values).filter { it.fileName.toString().startsWith("strings") }.flatMap {
                    it.getDocumentElement().childElements().stream().filter { it.tagName == "plurals" }
                            .map { it.getAttribute("name") }
                }.collect(Collectors.toList())
            }


    fun String.toPluralsFunNAme() = decapitalizeAsciiOnly().replace('.','_')


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
            .writeTo(generatedCommonSources(modules.modelcontract))


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
                            .addCode("return compute(R.plurals.${it.replace('.','_')}, quantity, *formatArgs)")
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(feature ?: modules.app))


}