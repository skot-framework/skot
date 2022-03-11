package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.childElements
import tech.skot.tools.generation.fileClassBuilder
import tech.skot.tools.generation.getDocumentElement
import java.nio.file.Files
import java.util.stream.Collectors

fun Generator.generateDimens() {

    println("dimens .........")
    println("generate Dimens interface .........")
    val values = rootPath.resolve(modules.view).resolve("src/androidMain/res_referenced/values")


    val dimens =
        if (!Files.exists(values)) {
            emptyList()
        } else {
            Files.list(values).flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "dimen" }
                    .map { it.getAttribute("name") }
            }.collect(Collectors.toList())
        }


    fun String.toDimenPropertyName() = decapitalizeAsciiOnly().replace('.', '_')

    val dimensAndroidRes: MutableList<PropertySpec> = mutableListOf()
    val dimensJVmMock: MutableList<PropertySpec> = mutableListOf()
    val dimensInt: MutableList<PropertySpec> = mutableListOf()
    dimens.forEach {
        dimensAndroidRes.add(
            PropertySpec.builder(
                it.toDimenPropertyName(),
                tech.skot.core.view.DimenRef::class,
                KModifier.OVERRIDE
            )
                .initializer("DimenRef(R.dimen.${it.toAndroidResourcePropertyName()})")
                .build()
        )
        dimensJVmMock.add(
            PropertySpec.builder(
                it.toDimenPropertyName(),
                tech.skot.core.view.DimenRef::class,
                KModifier.OVERRIDE
            )
                .initializer("DimenRef(\"${it.toDimenPropertyName()}\".hashCode())")
                .build()
        )
        dimensInt.add(
            PropertySpec.builder(it.toDimenPropertyName(), tech.skot.core.view.DimenRef::class)
                .build()
        )

    }

    FileSpec.builder(
        dimensInterface.packageName,
        dimensInterface.simpleName
    ).addType(
        TypeSpec.interfaceBuilder(dimensInterface.simpleName)
            .addProperties(dimensInt)
            .build()
    )
        .build()
        .writeTo(generatedCommonSources(modules.viewcontract))

    println("generate Dimens android implementation .........")


    dimensImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(dimensInterface)
        addProperties(dimensAndroidRes)
    }.writeTo(generatedAndroidSources(feature ?: modules.app))

    println("generate Dimens fot View Android Test .........")
    dimensImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(dimensInterface)
        addProperties(dimensAndroidRes)
    }.writeTo(generatedAndroidTestSources(modules.view))

    println("generate Dimens jvm mock .........")
    dimensMock.fileClassBuilder() {
        addSuperinterface(dimensInterface)
        addProperties(dimensJVmMock)
    }.writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))


}