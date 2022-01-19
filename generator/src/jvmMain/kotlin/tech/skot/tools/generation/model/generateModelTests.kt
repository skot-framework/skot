package tech.skot.tools.generation.model

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import tech.skot.tools.generation.AndroidClassNames
import tech.skot.tools.generation.FrameworkClassNames
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.fileClassBuilder

@ExperimentalStdlibApi
fun Generator.generateModelTests() {

    val abstractTestModel = ClassName(packageName = "$appPackage.screens", "TestModel")
    if (!abstractTestModel.existsJvmTestInModule(modules.model)) {
        abstractTestModel.fileClassBuilder(
            listOfNotNull(
                FrameworkClassNames.skTestModel,
                FrameworkClassNames.modelFrameworkModule,
                FrameworkClassNames.mockHttp,
                rootState?.modelClassName,
                rootState?.infosClassName,
                rootState?.let { ClassName("$appPackage.states", rootStatePropertyName!!) })
        ) {
            superclass(FrameworkClassNames.skTestModel)
            addModifiers(KModifier.ABSTRACT)
            addSuperclassConstructorParameter("${FrameworkClassNames.modelFrameworkModule.simpleName}/*add here model's modules*/")

            rootState?.let {
                addFunction(
                    FunSpec.builder("initStates")
                        .addAnnotation(AndroidClassNames.Annotations.before)
                        .addStatement("mockHttp.init()")
                        .addStatement("$rootStatePropertyName = ${it.modelClassName.simpleName}(${it.infosClassName.simpleName}())")
                        .build()
                )
            }

        }.writeTo(jvmTestSources(modules.model))
    }
    components.forEach {

        if (it.hasModel() && !it.testModel().existsJvmTestInModule(modules.model)) {
            it.testModel().fileClassBuilder {
                superclass(abstractTestModel)
            }.writeTo(jvmTestSources(modules.model))
        }

    }
}