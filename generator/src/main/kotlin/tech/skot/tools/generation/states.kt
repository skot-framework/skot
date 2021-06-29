package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly

@ExperimentalStdlibApi
fun Generator.generateStates(rootState: StateDef) {

    fun StateDef.generate() {

        contractClassName.fileInterfaceBuilder {
            (subStates + compositeSubStates).forEach {
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
            compositeParts.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.name.decapitalizeAsciiOnly(),
                        it.contract
                    ).build()
                )
            }

        }.writeTo(generatedCommonSources(Modules.modelcontract))


        if (!isCompositeState) {
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
            }.writeTo(generatedCommonSources(Modules.modelcontract))
        }


        modelClassName.fileClassBuilder(
            imports = if (compositeSubStates.isNotEmpty()) {
                listOf(
                    FrameworkClassNames.mapSKData,
                    FrameworkClassNames.combineSKData,
                    FrameworkClassNames.skData
                )
            } else {
                emptyList()
            } + bmS.map { it.withSuffix("Impl") }
        ) {
            addSuperinterface(contractClassName)
            addSuperinterface(kclass)


            if (isCompositeState) {
                addPrimaryConstructorWithParams(
                    parentsList.map {
                        ParamInfos(it.name.decapitalizeAsciiOnly(), it.modelClassName)
                    } + compositeParts.map {
                        ParamInfos(it.name.decapitalizeAsciiOnly(), it.model, modifiers = listOf(KModifier.OVERRIDE))
                    }
                )
                compositeParts.forEach {
                    addSuperinterface(
                        it.kClass,
                        delegate = CodeBlock.of(it.name.decapitalizeAsciiOnly())
                    )
                }

                //parent key (sauf root qui n'en a ps
                val parentKey = parentsList.drop(1).lastOrNull()?.let { it.name.decapitalizeAsciiOnly() }
                val thisInfosKey = compositeParts.map { it.name.decapitalizeAsciiOnly() }
                addProperty(
                    PropertySpec.builder("key", String::class)
                        .initializer(
                            CodeBlock.of(
                                (listOfNotNull(parentKey) + thisInfosKey).map { "\${$it.key}" }
                                    .joinToString(separator = "_", prefix = "\"", postfix = "\"")
                            )
                        )
                        .build()

                )


            } else {
                addPrimaryConstructorWithParams(
                    parentsList.map {
                        ParamInfos(it.name.decapitalizeAsciiOnly(), it.modelClassName)
                    } + ParamInfos("infos", this@generate.infosClassName, isVal = false)
                )


                val keyProperties = properties.filter { !it.mutable }
                if (keyProperties.isNotEmpty()) {
                    val keyInitializer = if (keyProperties.size == 1 && parentsList.size == 1) {
                        val uniqueProp = keyProperties.first()
                        "infos.${uniqueProp.name}${if (uniqueProp.typeName.simpleName() != "String") ".toString()" else ""}"
                    } else {
                        (listOfNotNull(
                            parentsList.drop(1).lastOrNull()
                                ?.let { "${it.name.decapitalizeAsciiOnly()}.key" }) +
                                keyProperties.map { "infos.${it.name}" }
                                ).map {
                                "\${$it}"
                            }.joinToString(separator = "_", prefix = "\"", postfix = "\"")
//                        keyProperties.map { "\${infos.${it.name}}" }
//                            .joinToString(separator = "_", prefix = "\"", postfix = "\"")
                    }
                    addProperty(
                        PropertySpec.builder("key", String::class)
                            .initializer(CodeBlock.of(keyInitializer))
                            .build()
                    )
                }
                else {
                    //on est dans le root state
                    addProperty(
                        PropertySpec.builder("key", String::class.asTypeName().nullable())
                            .initializer(CodeBlock.of("null"))
                            .build()
                    )
                }

            }



            if (!isCompositeState) {
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



            compositeSubStates.forEach {

                val newComposingList =
                    it.propertiesComposingComposite?.map { "new${it.nameAsProperty.capitalizeAsciiOnly()}" }
                val newComposingListWithNames =
                    it.propertiesComposingComposite?.map { "${it.name.decapitalizeAsciiOnly()} = new${it.nameAsProperty.capitalizeAsciiOnly()}" }

                val compositeConstructorParamsRoot =
                    (parentsList.map { it.nameAsProperty } + "this")
                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty.suffix("SKData"),
                        FrameworkClassNames.skData.parameterizedBy(it.modelClassName.nullable())
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer(
                            CodeBlock.builder()
                                .beginControlFlow(
                                    "combineSKData(${
                                        it.propertiesComposingComposite?.map {
                                            it.nameAsProperty.suffix(
                                                "SKData"
                                            )
                                        }?.joinToString()
                                    }).map"
                                )
                                .addStatement("(${newComposingList?.joinToString()}) ->")
                                .beginControlFlow(
                                    "if (${
                                        newComposingList?.map { "$it != null" }
                                            ?.joinToString(" && ")
                                    })"
                                )
                                .addStatement("${it.modelClassName.simpleName}(${(compositeConstructorParamsRoot + newComposingListWithNames!!).joinToString()})")
                                .endControlFlow()
                                .beginControlFlow("else")
                                .addStatement("null")
                                .endControlFlow()
                                .endControlFlow()
                                .build()
                        )
                        .build()
                )

                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty,
                        it.modelClassName.nullable()
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .getter(
                            FunSpec.getterBuilder()
                                .addCode("return ${it.nameAsProperty.suffix("SKData")}._current?.data")
                                .build()
                        )
                        .build()

                )


            }



            if (!isCompositeState) {
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
            }


            bmS.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.simpleName.decapitalize(),
                        it
                    ).initializer(
                        (listOf("key") + (parentsList.map {
                            it.name.decapitalizeAsciiOnly()
                        }) + "this").joinToString(
                            separator = ", ",
                            prefix = "${it.simpleName}Impl(",
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

        compositeSubStates.forEach {
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

