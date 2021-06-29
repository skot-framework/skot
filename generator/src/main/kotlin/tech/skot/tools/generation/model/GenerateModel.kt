package tech.skot.tools.generation.model

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.*
import kotlin.reflect.KParameter

@ExperimentalStdlibApi
fun Generator.generateModel() {
    println("-----generateModel")
    components.forEach {
        if (it.hasModel()) {
            //un model a été défini (par convention de nommage)
            //on va générer l'implémenation si elle n'existe pas encore et l'intégrer au modelInjector

            println("Un Model contract trouvé pour ${it.name}")

            if (!it.model().existsCommonInModule(Modules.model)) {
                println("pas d'implémentation trouvée on génère un squelette")

                it.model().fileClassBuilder {
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
                                it.stateDef()!!.modelClassName,
                                listOf(KModifier.OVERRIDE)
                            )
                        }
                    )

                    it.modelClass!!.ownProperties().forEach {
                        addProperty(
                            PropertySpec.builder(it.name, it.returnType.asTypeName())
                                .addModifiers(KModifier.OVERRIDE)
                                .build()
                        )
                    }

                    it.modelClass!!.ownFuncs().forEach {
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
                                .build()
                        )
                    }
                }
                    .writeTo(commonSources(Modules.model))
            }
        }
    }

    println("génération des Business Models")

    rootState?.addBmsTo(bmsMap)
    bmsMap.forEach { (className, state) ->
        if (!className.existsCommonInModule(Modules.model)) {
            FileSpec.builder(
                className.packageName,
                className.simpleName
            )
                .addType(
                    TypeSpec.interfaceBuilder(className.simpleName)
                        .build()
                )
                .addType(
                    TypeSpec.classBuilder(className.simpleName + "Impl")
                        .apply {
                            addPrimaryConstructorWithParams(
                                listOf(
                                    ParamInfos(
                                        "key",
                                        String::class.asTypeName().nullable(),
                                        isVal = false
                                    )
                                ) +
                                        state.parentsList.map {
                                            ParamInfos(
                                                it.name.decapitalizeAsciiOnly(),
                                                it.modelClassName,
                                                isPrivate = true
                                            )
                                        } + ParamInfos(
                                    state.name.decapitalizeAsciiOnly(),
                                    state.modelClassName,
                                    isPrivate = true
                                )
                            )
                            superclass(FrameworkClassNames.bm)
                            addSuperinterface(className)
                            addSuperclassConstructorParameter("key")
                        }
                        .build()
                )

                .build()

                .writeTo(commonSources(Modules.model))
        }
    }

    modelInjectorImpl.fileClassBuilder(
        imports = componentsWithModel.map { it.model() } + componentsWithModel.flatMap {
            it.states.map {
                it.stateDef()!!.modelClassName
            }
        })
    {
        addSuperinterface(modelInjectorInterface)
        addFunctions(
            componentsWithModel.map {
                FunSpec.builder(it.name.decapitalize())
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
                        "return ${it.model().simpleName}(${
                            (listOf("coroutineContext") + it.states.map {
                                "${it.name} as ${it.stateDef()!!.modelClassName.simpleName}"
                            }).joinToString(separator = ", ")
                        })"
                    )
                    .build()
            }
        )

    }.writeTo(generatedCommonSources(Modules.model))
}