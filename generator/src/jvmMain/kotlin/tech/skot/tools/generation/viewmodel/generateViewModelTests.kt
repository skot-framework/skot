package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.core.components.SKNoVM
import tech.skot.tools.generation.AndroidClassNames
import tech.skot.tools.generation.FrameworkClassNames
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.fileClassBuilder
import kotlin.reflect.full.hasAnnotation

@ExperimentalStdlibApi
fun Generator.generateViewModelTests() {

    val abstractTestScreen = ClassName(packageName = "$appPackage.screens", "TestScreen")
    if (!abstractTestScreen.existsJvmTestInModule(modules.viewmodel)) {
        abstractTestScreen.fileClassBuilder(
            listOfNotNull(viewModelModuleMock,
                rootState?.mockClassName,
                rootState?.let { ClassName(shortCuts.packageName, rootStatePropertyName!!) })
        ) {
            superclass(FrameworkClassNames.skTestViewModel)
            addModifiers(KModifier.ABSTRACT)
            addSuperclassConstructorParameter("${viewModelModuleMock.simpleName}/*add here libraries' modules*/")

            rootState?.let {
                addFunction(
                    FunSpec.builder("initStates")
                        .addAnnotation(AndroidClassNames.Annotations.before)
                        .addCode("$rootStatePropertyName = ${it.mockClassName.simpleName}()")
                        .build()
                )
            }

        }.writeTo(jvmTestSources(modules.viewmodel))
    }
    components.forEach {

        if (!it.vc.hasAnnotation<SKNoVM>()) {
            it.viewModelTester().fileClassBuilder {
                primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("component", it.viewModel())
                        .build()
                )
                superclass(
                    FrameworkClassNames.skViewModelTester.parameterizedBy(
                        it.viewMock(),
                        if (it.hasModel()) it.modelMock() else Unit::class.asTypeName()
                    )
                )
                addSuperclassConstructorParameter("component")
            }.writeTo(generatedJvmTestSources(modules.viewmodel))
        }
        // do not create class with suffix if prefix class already exists
        if (!it.vc.hasAnnotation<SKNoVM>() && !it.testViewModel().existsJvmTestInModule(modules.viewmodel) && !it.oldTestViewModel().existsJvmTestInModule(modules.viewmodel)) {
            it.testViewModel().fileClassBuilder {
                superclass(abstractTestScreen)
                val componentVarName = if (it.isScreen) "screen" else "component"
                addFunction(
                    FunSpec.builder("tester")
                        .addParameter(componentVarName, it.viewModel())
                        .returns(it.viewModelTester())
                        .addCode("return ${it.viewModelTester().simpleName}($componentVarName)")
                        .build()
                )
            }.writeTo(jvmTestSources(modules.viewmodel))
        }

    }
}