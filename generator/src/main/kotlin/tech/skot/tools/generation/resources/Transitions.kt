package tech.skot.tools.generation.resources

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import tech.skot.core.view.SKTransition
import tech.skot.tools.generation.*
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
fun Generator.generateTransitions() {
    val transitionsNames = if (!transisitonsInterface.existsCommonInModule(Modules.viewcontract)) {
        transisitonsInterface.fileInterfaceBuilder {

        }.writeTo(commonSources(Modules.viewcontract))
        emptyList()
    } else {
        val skTransitionsType = typeOf<SKTransition>()
        val interfaceClass = Class.forName(transisitonsInterface.canonicalName).kotlin
        interfaceClass.ownProperties().filter {
            it.returnType.isSubtypeOf(skTransitionsType)
        }
            .map { it.name }
    }

    if (!transisitonsImpl.existsAndroidInModule(Modules.view)) {
        transisitonsImpl.fileClassBuilder {
            addSuperinterface(transisitonsInterface)
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
        } .writeTo(androidSources(Modules.view))
    }


}