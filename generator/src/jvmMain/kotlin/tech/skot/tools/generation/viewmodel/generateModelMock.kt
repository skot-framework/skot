package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.model.SKData
import tech.skot.tools.generation.*
import tech.skot.tools.generation.viewlegacy.callClassName
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
fun Generator.generateModelMock() {
    println("-----generateModelMock")
    components.forEach {
        if (it.hasModel()) {
            //un model a été défini (par convention de nommage)

            if (!it.modelMock().existsJvmTestInModule(modules.viewmodel)) {
                //pas d'implémentation spécifique trouvée on génère

                it.modelMock().fileClassBuilder {
                    addSuperinterface(it.modelContract())
                    addSuperinterface(FrameworkClassNames.coroutineScope)
                    addPrimaryConstructorWithParams(
                        listOf(
                            ParamInfos(
                                "coroutineContext",
                                FrameworkClassNames.coroutineContext,
                                listOf(KModifier.OVERRIDE)
                            )
                        )
                                + it.states.map {
                            ParamInfos(
                                it.name,
                                it.stateDef()!!.mockClassName,
                                listOf(KModifier.OVERRIDE)
                            )
                        }
                    )

                    it.modelClass!!.ownProperties().forEach {

                        when {
                            it.returnType.isString() || it.returnType.isNullableString() -> {
                                addProperty(
                                    PropertySpec.builder(it.name, it.returnType.asTypeName())
                                        .addModifiers(KModifier.OVERRIDE)
                                        .mutable(true)
                                        .initializer("\"${it.name}\"")
                                        .build()
                                )
                            }
                            it.returnType.isSubtypeOf(typeOf<SKData<*>>()) -> {
                                addProperty(
                                    PropertySpec.builder(
                                        it.name,
                                        FrameworkClassNames.skDataMock.parameterizedBy(it.returnType.arguments.first().type!!.asTypeName())
                                    )
                                        .addModifiers(KModifier.OVERRIDE)
                                        .initializer("${FrameworkClassNames.skDataMock.simpleName}(\"${it.name}\")")
                                        .build()
                                )
                            }
                            else -> {
                                val primInit = it.returnType.primitiveDefaultInit()
                                if (primInit != null) {
                                    addProperty(
                                        PropertySpec.builder(it.name, it.returnType.asTypeName())
                                            .addModifiers(KModifier.OVERRIDE)
                                            .mutable(true)
                                            .apply {
                                                initializer(primInit)
                                            }
                                            .build()
                                    )
                                }
                                else {
                                    addProperty(
                                        PropertySpec.builder(it.name, it.returnType.asTypeName())
                                            .addModifiers(KModifier.OVERRIDE)
                                            .mutable(true)
                                            .apply {
                                                if (it.returnType.isMarkedNullable) {
                                                    initializer("null")
                                                }
                                                else {
                                                    addModifiers(KModifier.LATEINIT)
                                                }
                                            }
                                            .build()
                                    )
                                }

                            }
                        }


                    }

                    it.modelClass.ownFuncs().forEach {
                        val withParams = it.parameters.size > 1
                        val returnsUnit = it.returnType.isUnit()
                        val callClassName = if (withParams) {
                            ClassName("", it.callClassName())
                        } else {
                            Unit::class.asTypeName()
                        }
                        if (withParams) {
                            addType(
                                TypeSpec.classBuilder(callClassName)
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
                                name = "${it.name}Mock",
                                type = if (returnsUnit) {
                                    FrameworkClassNames.skFunUnitMock.parameterizedBy(callClassName)
                                } else {
                                    FrameworkClassNames.skFunMock.parameterizedBy(
                                        callClassName,
                                        it.returnType.asTypeName()
                                    )
                                }

                            )
                                .initializer(
                                    "${
                                        (if (returnsUnit) FrameworkClassNames.skFunUnitMock else FrameworkClassNames.skFunMock).simpleName
                                    }(\"${it.name}Mock\")"
                                )
                                .build()
                        )

                        fun callObject(func: KFunction<*>) = if (!withParams) {
                            "Unit"
                        } else {
                            it.parameters.filter { it.name != null }
                                .map { it.name!! }
                                .joinToString(
                                    prefix = "${callClassName.simpleName}(",
                                    postfix = ")",
                                    separator = ", "
                                )
                        }
                        addFunction(
                            FunSpec.builder(it.name)
                                .apply {
                                    if (it.isSuspend) {
                                        addModifiers(KModifier.SUSPEND)
                                    }
                                }
                                .addParameters(
                                    it.parameters.filter { it.kind == KParameter.Kind.VALUE }.map {
                                        ParameterSpec(it.name!!, it.type.asTypeName())
                                    }
                                )
                                .returns(it.returnType.asTypeName())
                                .addModifiers(KModifier.OVERRIDE)
                                .addStatement("return ${it.name}Mock(${callObject(it)})")
                                .build()
                        )
                    }
                }
                    .writeTo(generatedJvmTestSources(modules.viewmodel))
            }
        }
    }
}