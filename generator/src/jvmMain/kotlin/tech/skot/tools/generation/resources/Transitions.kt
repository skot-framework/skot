package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import tech.skot.core.view.SKTransition
import tech.skot.tools.generation.*
import tech.skot.tools.generation.FrameworkClassNames.skTransitionMock
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
fun Generator.generateTransitions() {
    val transitionsNames = if (!transitionsInterface.existsCommonInModule(modules.viewcontract)) {
        transitionsInterface.fileInterfaceBuilder {

        }.writeTo(commonSources(modules.viewcontract))
        emptyList()
    } else {
        val skTransitionsType = typeOf<SKTransition>()
        val interfaceClass = Class.forName(transitionsInterface.canonicalName).kotlin
        interfaceClass.ownProperties().filter {
            it.returnType.isSubtypeOf(skTransitionsType)
        }
            .map { it.name }
    }

    if (!transitionsImpl.existsAndroidInModule(modules.view)) {
        transitionsImpl.fileClassBuilder {
            addSuperinterface(transitionsInterface)
            addProperties(
                transitionsNames.map {
                    PropertySpec.builder(
                        name = it,
                        type = FrameworkClassNames.transistionAndroidLegacy
                    )
                        .initializer(CodeBlock.of("${FrameworkClassNames.transistionAndroidLegacy.simpleName}(enterAnim = ???, exitAnim = ???)"))
                        .addModifiers(KModifier.OVERRIDE)
                        .build()
                }
            )
        } .writeTo(androidSources(feature ?: modules.view))
    }

    println("generate Transitions jvm mock .........")
    transitionsMock.fileClassBuilder() {
        addSuperinterface(transitionsInterface)
        addProperties(
            transitionsNames.map {
                PropertySpec.builder(it, skTransitionMock, KModifier.OVERRIDE)
                    .initializer("SKTransitionMock(\"$it\")")
                    .build()
            }
        )
    }
        .writeTo(generatedJvmTestSources(feature ?: modules.viewmodel))
}