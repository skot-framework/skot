package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.FrameworkClassNames
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.fileClassBuilder

@ExperimentalStdlibApi
fun Generator.generateModelInjectorMock() {

    modelInjectorMock.fileClassBuilder(
        imports = componentsWithModel.map { it.modelMock() } + componentsWithModel.flatMap {
            it.states.map {
                it.stateDef()!!.mockClassName
            }
        })
    {
        addSuperinterface(modelInjectorInterface)
        addFunctions(
            componentsWithModel.map {
                FunSpec.builder(it.name.decapitalizeAsciiOnly())
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        ParameterSpec.builder(
                            "coroutineContext",
                            FrameworkClassNames.coroutineContext
                        )
                            .build()
                    )
                    .addParameters(
                        it.states.map {
                            ParameterSpec.builder(it.name, it.stateDef()!!.contractClassName)
                                .build()
                        }
                    )
                    .returns(it.modelContract())
                    .addCode(
                        "return ${it.modelMock().simpleName}(${
                            (listOf("coroutineContext") + it.states.map {
                                "${it.name} as ${it.stateDef()!!.mockClassName.simpleName}"
                            }).joinToString(separator = ", ")
                        })"
                    )
                    .build()
            }
        )

    }.writeTo(generatedJvmTestSources(modules.viewmodel))
}