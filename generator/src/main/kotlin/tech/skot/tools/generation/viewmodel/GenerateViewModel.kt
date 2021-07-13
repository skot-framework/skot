package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.*

@ExperimentalStdlibApi
fun Generator.generateViewModel() {
    components.forEach {
        it.viewModelGen().fileClassBuilder(
            listOf(modelInjectorIntance)
        ) {
            addModifiers(KModifier.ABSTRACT)
            superclass(it.superVM.parameterizedBy(it.vc.asTypeName()))
            if (it.hasModel()) {
                if (it.states.isNotEmpty()) {
                    addPrimaryConstructorWithParams(
                        it.states.map {
                            ParamInfos(
                                it.name,
                                it.stateDef()?.contractClassName
                                    ?: throw IllegalStateException("To use states you have to set the root state in configuration of skot module"),
                                isVal = false
                            )
                        }
                    )
                }

                addProperty(
                    PropertySpec.builder("model", it.modelContract())
                        .initializer(
                            "modelInjector.${it.name.decapitalize()}(${
                                (listOf("coroutineContext") + it.states.map { it.name }).joinToString(
                                    ", "
                                )
                            })"
                        )
                        .build()
                )


            }

            it.subComponents.forEach {
                if (it.name != "loader") {
                    addProperty(
                        PropertySpec.builder(
                            it.name,
                            FrameworkClassNames.skComponent,
                            KModifier.ABSTRACT,
                            KModifier.PROTECTED
                        )
                            .build()
                    )
                }
            }

            addFunction(FunSpec.builder("onRemove")
                .addModifiers(KModifier.OVERRIDE)
                .apply {
                    it.subComponents.forEach {
                        if (it.name != "loader") {
                            addStatement("${it.name}.onRemove()")
                        } else {
                            addStatement("loader?.onRemove()")
                        }

                    }
                }
                .addStatement("super.onRemove()")
                .build())
        }
            .writeTo(generatedCommonSources(Modules.viewmodel))

        if (!it.viewModel().existsCommonInModule(Modules.viewmodel)) {
            it.viewModel()
                .fileClassBuilder(it.subComponents.map { it.type.toVM() } + viewInjectorIntance) {
                    superclass(it.viewModelGen())

                    if (it.states.isNotEmpty()) {
                        addPrimaryConstructorWithParams(
                            it.states.map {
                                ParamInfos(
                                    it.name,
                                    it.stateDef()!!.contractClassName,
                                    isVal = false
                                )
                            }
                        )
                        it.states.forEach {
                            superclassConstructorParameters.add(CodeBlock.of(it.name))
                        }
                    }

                    it.subComponents.forEach {
                        val vmType = it.type.toVM()
                        addProperty(
                            PropertySpec.builder(it.name, vmType)
                                .initializer("${vmType.simpleName}()")
                                .addModifiers(KModifier.OVERRIDE)
                                .build()
                        )
                    }
                    addProperty(
                        PropertySpec.builder(
                            "view", it.vc.asTypeName(), KModifier.OVERRIDE
                        )
                            .initializer("viewInjector.${it.name.decapitalize()}(${it.toFillVCparams()})")
                            .build()
                    )
                    (it.fixProperties + it.mutableProperties).filter {
                        it.isLambda
                    }.forEach {
                        addFunction(
                            FunSpec.builder(it.name).addModifiers(KModifier.PRIVATE).build()
                        )
                    }
                }

                .writeTo(commonSources(Modules.viewmodel))

        }
    }

    FileSpec.builder(shortCuts.packageName, shortCuts.simpleName)
        .addProperty(
            PropertySpec
                .builder(viewInjectorIntance.simpleName, viewInjectorInterface)
                .initializer("get()")
                .build()
        )
        .addProperty(
            PropertySpec
                .builder(modelInjectorIntance.simpleName, modelInjectorInterface)
                .initializer("get()")
                .build()
        )
        .addProperty(
            PropertySpec
                .builder(stringsInstance.simpleName, stringsInterface)
                .initializer("get()")
                .build()
        )
        .addProperty(
            PropertySpec
                .builder(pluralsInstance.simpleName, pluralsInterface)
                .initializer("get()")
                .build()
        )
        .addProperty(
            PropertySpec
                .builder(iconsInstance.simpleName, iconsInterface)
                .initializer("get()")
                .build()
        )
        .addProperty(
            PropertySpec
                .builder(colorsInstance.simpleName, colorsInterface)
                .initializer("get()")
                .build()
        )
        .addProperty(
            PropertySpec
                .builder("transitions", transisitonsInterface)
                .initializer("get()")
                .build()
        )
        .apply {
            if (rootState != null) {
                addProperty(
                    PropertySpec.builder(
                        rootState.nameAsProperty,
                        rootState.contractClassName,
                        KModifier.LATEINIT
                    )
                        .mutable()
                        .build()
                )
                addImportClassName(rootState.contractClassName)
            }
        }

        .addImportClassName(getFun)
        .addImportClassName(stringsInterface)
        .addImportClassName(pluralsInterface)
        .addImportClassName(colorsInterface)
        .addImportClassName(viewInjectorInterface)

        .build()
        .writeTo(generatedCommonSources(Modules.viewmodel))

    val start = ClassName(appPackage, "start")
    if (!start.existsCommonInModule(Modules.viewmodel)) {
        val startViewModel = components.first().viewModel()
        FileSpec.builder(start.packageName, start.simpleName)
            .addFunction(
                FunSpec
                    .builder("start")
                    .addParameter("unit", UNIT)
                    .addCode("SKRootStack.content = ${startViewModel.simpleName}()")
                    .build()
            )
            .addImport("tech.skot.core.components", "SKRootStack")
            .addImportClassName(startViewModel)
            .build()
            .writeTo(commonSources(Modules.viewmodel))
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
