package tech.skot.generator

import com.squareup.kotlinpoet.*
import tech.skot.components.ComponentView
import tech.skot.generator.utils.*
import java.nio.file.Paths
import kotlin.reflect.KClass


fun buildViewInjectorInterface(moduleName: String) {
    FileSpec.builder(appPackageName, "ViewInjector")
            .apply {
                addType(TypeSpec.interfaceBuilder("ViewInjector")
                        .addFunctions(
                                actualComponents
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
                .addParameters(propertyMember().map {
                    ParameterSpec.builder(it.name, it.returnType.asTypeName()).apply {
                        if (it.returnType.isMarkedNullable) {
                            defaultValue(CodeBlock.of("null"))
                        }
                    }.build()
                })
                .build()
