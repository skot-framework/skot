package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
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

        }.writeTo(generatedCommonSources(modules.modelcontract))


        if (!isCompositeState) {
            infosClassName.fileClassBuilder {
//            addSuperinterface(contractClassName)
                addModifiers(KModifier.DATA)
                addAnnotation(Serializable::class)
                addPrimaryConstructorWithParams(
                    properties.map {
                        ParamInfos(it.name, it.typeName, default = it.default )
                    } +
                            subStates.map {
                                ParamInfos(
                                    it.nameAsProperty,
                                    it.infosClassName.nullable(),
                                    default = "null"
                                )
                            }
                )
            }.writeTo(generatedCommonSources(modules.modelcontract))
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
                    if (it.bySkData) {
                        addProperty(
                            PropertySpec.builder(
                                "${it.name}SKData",
                                FrameworkClassNames.skManualData.parameterizedBy(it.typeName)
                            )
                                .initializer(
                                    CodeBlock.of("${FrameworkClassNames.skManualData.simpleName}(infos.${it.name})")
                                )
                                .build()
                        )
                    }
                    addProperty(
                        PropertySpec.builder(
                            it.name,
                            it.typeName
                        ).mutable(it.mutable)
                            .apply {
                                if (it.mutable) {
                                    if (it.bySkData) {
                                        getter(
                                            FunSpec.getterBuilder()
                                                .addStatement("return ${it.name}SKData.value")
                                                .build()
                                        )
                                        setter(
                                            FunSpec.setterBuilder()
                                                .addParameter("newValue", it.typeName)
                                                .addStatement("${it.name}SKData.value = newValue")
                                                .addStatement("saveState()")
                                                .build()
                                        )
                                    }
                                    else {
                                        setter(
                                            FunSpec.setterBuilder()
                                                .addParameter("newValue", it.typeName)
                                                .addStatement("field = newValue")
                                                .addStatement("saveState()")
                                                .build()
                                        )
                                    }
                                }

                                if (!it.bySkData) {
                                    initializer("infos.${it.name}")
                                }

                            }
                            .addModifiers(KModifier.OVERRIDE)
                            .build()
                    )
                }
            }


            val subStateConstructorParams =
                (parentsList.map { it.name.decapitalizeAsciiOnly() } + "this" + "it")
                    .joinToString(", ")

            subStates.forEach {
                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty.suffix("SKData"),
                        FrameworkClassNames.skManualData.parameterizedBy(it.modelClassName.nullable())
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer(
                            CodeBlock.builder()
                                .beginControlFlow("${FrameworkClassNames.skManualData.simpleName}(infos.${it.nameAsProperty}?.let { ${it.modelClassName.simpleName}($subStateConstructorParams) })")
                                .apply {
                                    compositeSubStates.forEach { compositeSubState ->
                                        if (compositeSubState.propertiesComposingComposite!!.contains(it)) {
                                            addStatement("update${compositeSubState.name}()")
                                        }
                                    }
                                }
                                .addStatement("saveState()")
                                .endControlFlow()
                                .build())
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

                //méthode computeState
                addFunction(FunSpec.builder("compute${it.nameAsProperty}")
                    .addParameters(
                        it.propertiesComposingComposite!!.map {
                            ParameterSpec.builder(it.name.decapitalizeAsciiOnly(), it.modelClassName.nullable())
                                .build()
                        }
                    )
                    .returns(it.modelClassName.nullable())
                    .beginControlFlow("return if (${it.propertiesComposingComposite!!.map { "${it.name.decapitalizeAsciiOnly() } != null" }.joinToString(" && ")})")
                    .addStatement("${it.name}(this, ${it.propertiesComposingComposite!!.map { it.name.decapitalizeAsciiOnly() }.joinToString()})")
                    .endControlFlow()
                    .beginControlFlow("else")
                    .addStatement("null")
                    .endControlFlow()
                    .build())


                addFunction(FunSpec.builder("update${it.name}")
                    .addStatement("${it.nameAsProperty.suffix("SKData")}.value = compute${it.nameAsProperty}(${it.propertiesComposingComposite!!.map { it.nameAsProperty }.joinToString()})")
                    .build())
                
                addProperty(
                    PropertySpec.builder(
                        it.nameAsProperty.suffix("SKData"),
                        FrameworkClassNames.skManualData.parameterizedBy(it.modelClassName.nullable())
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer(
                            CodeBlock.of("${FrameworkClassNames.skManualData.simpleName}(compute${it.nameAsProperty}(${it.propertiesComposingComposite.map { it.nameAsProperty }.joinToString()}))")

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
            .writeTo(generatedCommonSources(modules.model))

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
    if (!statePersistenceManager.existsCommonInModule(modules.model)) {
        val keyName = "${rootStatePropertyName!!.uppercase()}_GLOBAL_KEY"
        FileSpec.builder(statePersistenceManager.packageName, statePersistenceManager.simpleName)
            .addProperty(
                PropertySpec.builder(keyName, String::class)
                    .addModifiers(KModifier.CONST)
                    .initializer("\"${rootStatePropertyName!!.uppercase()}\"")
                    .build()
            )
            .addProperty(
                PropertySpec.builder(rootStatePropertyName!!, rootState.modelClassName)
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
                    .addStatement("data = $rootStatePropertyName.infos()")
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
            .writeTo(commonSources(modules.model))
    }


    println("... fin génération des états")

}

