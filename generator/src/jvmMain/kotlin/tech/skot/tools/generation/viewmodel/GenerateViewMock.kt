package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.*
import tech.skot.tools.generation.viewlegacy.dataClassName
import kotlin.reflect.KFunction

@ExperimentalStdlibApi
fun Generator.generateViewMock() {
    components.forEach {

        it.viewMock().fileClassBuilder {
            superclass(if (it.isScreen) FrameworkClassNames.skScreenViewMock else FrameworkClassNames.skComponentViewMock)
            addSuperinterface(it.vc)
            addPrimaryConstructorWithParams(


                    it.subComponents.map {
                        ParamInfos(
                                name = it.name,
                                typeName = it.type,
                                modifiers = listOf(KModifier.OVERRIDE),
                                isVal = true
                        )
                    } +
                            it.fixProperties.map {
                                ParamInfos(
                                        name = it.name,
                                        typeName = it.type,
                                        modifiers = listOf(KModifier.OVERRIDE),
                                        isVal = true
                                )
                            } +
                            it.mutableProperties.map {
                                ParamInfos(
                                        name = it.name.initial(),
                                        typeName = it.type,
                                        isVal = false
                                )
                            }
            )
            it.mutableProperties.forEach {
                                    addProperty(
                            PropertySpec.builder(
                                    it.name,
                                    it.type,
                                    KModifier.PUBLIC,
                                    KModifier.OVERRIDE
                            ).mutable(true)
                                    .initializer(it.name.initial())
                                    .build()
                    )
            }
            it.ownFunctions.forEach {
                val withParams = it.parameters.size > 1
                val dataClassName = it.dataClassName()
                val callsProperty = "${it.name}Calls"
                if (withParams) {
                    addType(
                        TypeSpec.classBuilder(dataClassName)
                            .addModifiers(KModifier.DATA)
                            .addPrimaryConstructorWithParams(it.parameters.mapNotNull { kParam ->
                                kParam.name?.let {
                                    ParamInfos(it, kParam.type.asTypeName())
                                }
                            })
                            .build()
                    )
                }
                addProperty(
                    PropertySpec.builder(
                        callsProperty, ClassName("kotlin.collections", listOf("MutableList")).parameterizedBy(
                            if (withParams) {
                                ClassName("", dataClassName)
                            } else {
                                Unit::class.asTypeName()
                            }
                        )
                    )
                        .initializer("mutableListOf()").build()
                )
                fun callObject(func: KFunction<*>) = if (func.parameters.size == 1) {
                    "Unit"
                } else {
                    it.parameters.filter { it.name != null }
                        .map { it.name!! }
                        .joinToString(prefix = "$dataClassName(", postfix = ")", separator = ", ")
                }
                addFunction(
                    FunSpec.builder(it.name)
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameters(it.parameters.mapNotNull { kParam ->
                            kParam.name?.let {
                                ParameterSpec.builder(it, kParam.type.asTypeName()).build()
                            }
                        })
                        .addCode("$callsProperty.add(${callObject(it)})")
                        .build()
                )

            }

        }.writeTo(generatedJvmTestSources(modules.viewmodel))
    }
}

fun TypeName.toViewMock() =
        (this as ClassName).let { ClassName(it.packageName, it.simpleName.toViewMock()) }

fun String.toViewMock() = when {
    endsWith("VC") -> {
        substring(0, indexOf("VC")).suffix("ViewMock")
    }
    else -> {
        suffix("ViewMock")
    }
}