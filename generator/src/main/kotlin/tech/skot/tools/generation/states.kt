package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable

@ExperimentalStdlibApi
fun Generator.generateStates(rootState: StateDef) {

    fun StateDef.generate() {

        contractClassName.fileInterfaceBuilder {
            subStates.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty,
                        it.contractClassName.nullable()
                    )
                        .build()
                )
                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty.suffix("SKData"),
                        FrameworkClassNames.skData.parameterizedBy(WildcardTypeName.producerOf(it.contractClassName.nullable()))
                    )
                        .build()
                )
            }

        }.writeTo(generatedCommonSources(Modules.modelcontract))


        infosClassName.fileClassBuilder {
//            addSuperinterface(contractClassName)
            addModifiers(KModifier.DATA)
            addAnnotation(Serializable::class)
            addPrimaryConstructorWithParams(
                properties.map {
                    ParamInfos(it.name, it.typeName)
                } +
                        subStates.map {
                            ParamInfos(
                                it.nameAsProperty,
                                it.infosClassName.nullable(),
                                default = "null"
                            )
                        }
            )
        }.writeTo(generatedCommonSources(Modules.model))

        modelClassName.fileClassBuilder {
            addSuperinterface(contractClassName)
            addSuperinterface(kclass)
            addPrimaryConstructorWithParams(
                parentsList.map {
                    ParamInfos(it.nameAsProperty.decapitalize(), it.modelClassName)
                } + ParamInfos("infos", this@generate.infosClassName, isVal = false)
            )

            properties.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.name,
                        it.typeName
                    ).mutable(it.mutable)
                        .apply {
                            if (it.mutable) {
                                setter(
                                    FunSpec.setterBuilder()
                                        .addParameter("newValue", it.typeName)
                                        .addStatement("field = newValue")
                                        .addStatement("saveState()")
                                        .build()
                                )
                            }
                        }
                        .initializer("infos.${it.name}")
                        .addModifiers(KModifier.OVERRIDE)
                        .build()
                )
            }

            val subStateConstructorParams =
                (parentsList.map { it.nameAsProperty } + "this" + "it")
                    .joinToString(", ")

            subStates.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty.suffix("SKData"),
                        FrameworkClassNames.skManualData.parameterizedBy(it.modelClassName.nullable())
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("${FrameworkClassNames.skManualData.simpleName}(infos.${it.nameAsProperty}?.let { ${it.modelClassName.simpleName}($subStateConstructorParams) }) { saveState() }")
                        .build()
                )

                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty,
                        it.modelClassName.nullable()
                    )
                        .mutable(true)
                        .addModifiers(KModifier.OVERRIDE)
                        .delegate(it.nameAsProperty.suffix("SKData"))
                        .build()

                )
            }

            addFunction(
                FunSpec.builder("infos")
                    .addCode("return ${infosClassName.simpleName}(\n")
                    .apply {
                        properties.forEach {
                            addStatement("${it.name} = ${it.name},")
                        }
                        subStates.forEach {
                            addStatement("${it.nameAsProperty} = ${it.nameAsProperty}?.infos(),")
                        }
                    }
                    .addCode(")\n")
                    .returns(infosClassName)
                    .build())

            bmS.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.simpleName.decapitalize(),
                        it
                    ).initializer(
                        (parentsList.map {
                            it.nameAsProperty
                        } + "this").joinToString(
                            separator = ", ",
                            prefix = "${it.simpleName}(",
                            postfix = ")"
                        )
                    )
                        .build()
                )
            }
        }
            .writeTo(generatedCommonSources(Modules.model))

        subStates.forEach {
            it.generate()
        }
    }

    println("génération des états...")
    rootState.generate()

//    FileSpec.builder("$appPackage.states", "rootStateShortCut")
//        .addProperty(
//            PropertySpec.builder(
//                rootState.nameAsProperty,
//                rootState.contractClassName,
//                KModifier.LATEINIT
//            )
//                .mutable()
//                .build()
//        )
//        .build()
//        .writeTo(generatedCommonSources(Modules.viewmodel))

    println("génération de statePersistanceManager")
    if (!statePersistenceManager.existsCommonInModule(Modules.model)) {
        val keyName = "${rootState.nameAsProperty.uppercase()}_GLOBAL_KEY"
        FileSpec.builder(statePersistenceManager.packageName, statePersistenceManager.simpleName)
            .addProperty(
                PropertySpec.builder(keyName, String::class)
                    .addModifiers(KModifier.CONST)
                    .initializer("\"${rootState.nameAsProperty.uppercase()}\"")
                    .build()
            )
            .addProperty(
                PropertySpec.builder(rootState.nameAsProperty, rootState.modelClassName)
                    .addModifiers(KModifier.LATEINIT)
                    .mutable()
                    .build()
            )
            .addFunction(
                FunSpec.builder(saveStateFunction.simpleName)
                    .beginControlFlow("GlobalScope.launch")
                    .addStatement("${FrameworkClassNames.globalCache.simpleName}.putData(")
//                    .beginControlFlow("${rootState.nameAsProperty}.let")
                    .addStatement("serializer = ${rootState.infosClassName.simpleName}.serializer(),")
                    .addStatement("name = $keyName,")
                    .addStatement("data = ${rootState.nameAsProperty}.infos()")
                    .addStatement(")")
                    .endControlFlow()
                    .build()
            )
            .addFunction(
                FunSpec.builder(restoreStateFunction.simpleName)
                    .addModifiers(KModifier.SUSPEND)
                    .addCode("return ${FrameworkClassNames.globalCache.simpleName}.getData(\n")
                    .addStatement("serializer = ${rootState.infosClassName.simpleName}.serializer(),")
                    .addStatement("name = $keyName")
                    .addCode(")?.\n")
                    .addStatement("let { ${rootState.modelClassName.simpleName}(it) }")
                    .addStatement(
                        "?: ${rootState.modelClassName.simpleName}(${rootState.infosClassName.simpleName}(${
                            rootState.subStates.joinToString(
                                separator = ", "
                            ) { "null" }
                        }))"
                    )
                    .returns(rootState.modelClassName)
                    .build()
            )
            .addImportClassName(FrameworkClassNames.globalCache)
            .addImportClassName(FrameworkClassNames.globalScope)
            .addImportClassName(FrameworkClassNames.launch)
//            .addImportClassName(rootState.infosClassName)
            .build()
            .writeTo(commonSources(Modules.model))
    }


    println("... fin génération des états")

}

