package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName
import tech.skot.tools.generation.AndroidClassNames
import tech.skot.tools.generation.FrameworkClassNames
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.fileClassBuilder

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

        if (!it.testViewModel().existsJvmTestInModule(modules.viewmodel)) {
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