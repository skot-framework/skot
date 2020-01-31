package tech.skot.generator

import com.squareup.kotlinpoet.*
import tech.skot.components.ComponentView
import tech.skot.contract.Model
import java.nio.file.Paths
import kotlin.reflect.KClass

fun generateContract(moduleName: String) {
    buildViewInjectorInterface(moduleName)
    buildModelInjectorInterface(moduleName)
}

fun buildViewInjectorInterface(moduleName: String) {

    FileSpec.builder(appPackageName, "ViewInjector")
            .apply {
                addType(TypeSpec.interfaceBuilder("ViewInjector")
                        .addFunctions(
                                actualComponents
                                        .fromApp()
                                        .map {
                                            it.injectorsFunction()
                                        }).build())
            }.build().writeTo(Paths.get("../$moduleName/generated/commonMain/kotlin").toFile())
}


fun KClass<out ComponentView>.injectorsFunction() =
        FunSpec.builder(compName().decapitalize())
                .returns(this)
                .addModifiers(KModifier.ABSTRACT)
                .addParameters(subComponentMembers().map { ParameterSpec(it.name, it.returnType.asTypeName()) })
                .addParameters(allPropertyMember().map {
                    ParameterSpec.builder(it.name, it.returnType.asTypeName()).apply {
                        if (it.returnType.isMarkedNullable) {
                            defaultValue(CodeBlock.of("null"))
                        }
                    }.build()
                })
                .build()

fun buildModelInjectorInterface(moduleName: String) {
    FileSpec.builder(appPackageName, "ModelInjector")
            .apply {
                addType(TypeSpec.interfaceBuilder("ModelInjector")
                        .addFunctions(
                                allModelsFromApp
                                        .map {
                                            it.injectorsModelFunction()
                                        }
                                        .filterNotNull()).build())
            }.build().writeTo(Paths.get("../$moduleName/generated/commonMain/kotlin").toFile())
}

fun KClass<*>.injectorsModelFunction() =
        FunSpec.builder(simpleName!!.decapitalize())
                            .returns(this)
                            .addModifiers(KModifier.ABSTRACT)
                            .build()

