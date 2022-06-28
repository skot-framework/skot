package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.*

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

        componentsWithModel.forEach {
            val initName = it.name.decapitalizeAsciiOnly().suffix("Init")
            addProperty(
                PropertySpec.builder(
                    initName,
                    type = LambdaTypeName.get(
                        receiver = it.modelMock(),
                        returnType = UNIT).nullable(),
                ).mutable(true)
                    .initializer("null")
                    .build()
            )
            addFunction(
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
                    .beginControlFlow(
                        "return ${it.modelMock().simpleName}(${
                            (listOf("coroutineContext") + it.states.map {
                                "${it.name} as ${it.stateDef()!!.mockClassName.simpleName}"
                            }).joinToString(separator = ", ")
                        }).apply"
                    )
                    .addStatement("$initName?.invoke(this)")
                    .endControlFlow()
                    .build())
        }


    }.writeTo(generatedJvmTestSources(modules.viewmodel))
}