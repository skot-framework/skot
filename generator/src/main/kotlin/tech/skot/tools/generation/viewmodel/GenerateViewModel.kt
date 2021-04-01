package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.tools.generation.*
import tech.skot.tools.generation.viewlegacy.componentViewModel
import tech.skot.tools.generation.viewlegacy.screenViewModel

fun Generator.generateViewModel() {
    deleteModuleGenerated(Modules.viewmodel)
    components.forEach {
        it.viewModelGen().fileClassBuilder(
                listOf(modelInjectorIntance)
        ) {
            addModifiers(KModifier.ABSTRACT)
            superclass(it.superVM.parameterizedBy(it.vc.asTypeName()))
            if (it.hasModel()) {
                addProperty(
                        PropertySpec.builder("model", it.modelContract())
                                .initializer("modelInjector.${it.name.decapitalize()}()")
                                .build()
                )
            }
        }
                .writeTo(generatedCommonSources(Modules.viewmodel))

        if (!it.viewModel().existsCommonInModule(Modules.viewmodel)) {
            it.viewModel().fileClassBuilder(it.subComponents.map { it.type.toVM() } + viewInjectorIntance) {
                superclass(it.viewModelGen())
                it.subComponents.forEach {
                    val vmType = it.type.toVM()
                    addProperty(
                            PropertySpec.builder(it.name, vmType, KModifier.PRIVATE)
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

    FileSpec.builder("${appPackage}.di", "shortCuts")
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
                                .addCode("RootStack.screens = listOf(${startViewModel.simpleName}())")
                                .build()
                )
                .addImport("tech.skot.core.components", "RootStack")
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
