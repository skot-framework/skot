package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.Modules
import tech.skot.tools.generation.fileClassBuilder
import tech.skot.tools.generation.viewlegacy.componentViewModel
import tech.skot.tools.generation.viewlegacy.screenViewModel

fun Generator.generateViewModel() {
    deleteModuleGenerated(Modules.viewmodel)
    components.forEach {
        it.viewModelGen().fileClassBuilder {
            addModifiers(KModifier.ABSTRACT)
            superclass((if (it.isScreen) screenViewModel else componentViewModel).parameterizedBy(it.vc.asTypeName()))
        }
                .writeTo(generatedCommonSources(Modules.viewmodel))

        if (!it.viewModel().existsCommonInModule(Modules.viewmodel)) {
            it.viewModel().fileClassBuilder(it.subComponents.map { it.type.toVM() } + viewInjectorIntance) {
                superclass(it.viewModelGen())
                it.subComponents.forEach {
                    val vmType = it.type.toVM()
                    addProperty(
                            PropertySpec.builder(vmType.simpleName.decapitalize(), vmType, KModifier.PRIVATE)
                                    .initializer("${vmType.simpleName}()").build()
                    )
                }
                addProperty(PropertySpec.builder(
                        "view", it.vc.asTypeName(), KModifier.OVERRIDE)
                        .initializer("viewInjector.${it.name.decapitalize()}(${it.toFillVCparams()})")
                        .build()
                )
                (it.fixProperties + it.mutableProperties).filter {
                    it.isLambda
                }.forEach {
                    addFunction(FunSpec.builder(it.name).addModifiers(KModifier.PRIVATE).build())
                }
            }

                    .writeTo(commonSources(Modules.viewmodel))

        }
    }

}

fun String.toVM() = when {
    endsWith("VC") -> {
        substring(0, indexOf("VC"))
    }
    else -> {
        this
    }
}

fun TypeName.toVM() = (this as ClassName).let { ClassName(it.packageName, it.simpleName.toVM()) }
