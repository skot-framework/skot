package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.kotlin.util.capitalizeDecapitalize.decapitalizeAsciiOnly
import tech.skot.tools.generation.*
import tech.skot.tools.generation.viewlegacy.asViewLegacyInjection
import tech.skot.tools.generation.viewlegacy.toProxy
import tech.skot.tools.generation.viewlegacy.toView

fun Generator.generateViewInjectorMock() {

    FileSpec.builder(viewInjectorMock.packageName, viewInjectorMock.simpleName)
            .addType(TypeSpec.classBuilder(viewInjectorMock.simpleName)
                    .addSuperinterface(viewInjectorInterface)
                    .addFunctions(
                            components.map { it.asViewMockInjection(this) }
                    )
                    .build())
            .apply {
                components.forEach {
                    addImportClassName(it.viewMock())
                    it.subComponents.forEach {
                        addImportTypeName(it.type.toViewMock())
                    }
                }

            }
            .build()
            .writeTo(generatedJvmTestSources(modules.viewmodel))
}

fun ComponentDef.asViewMockInjection(generator: Generator) =
        FunSpec.builder(name.decapitalizeAsciiOnly())
                .addModifiers(KModifier.OVERRIDE)
                .apply {
                    if (isScreen) {
                        addParameter(name = Generator.VISIBILITY_LISTENER_VAR_NAME, type = FrameworkClassNames.skVisiblityListener)
                    }
                    subComponents.forEach {
                        addParameter(it.asParam())
                    }
                    fixProperties.forEach {
                        addParameter(it.asParam())
                    }
                    mutableProperties.forEach {
                        addParameter(it.initial().asParam())
                    }
                }
                .returns(vc)
                .addCode(
                        "return ${viewMock().simpleName}(${(subComponents.map { "${it.name} as ${it.type.toViewMock().simpleName()}" } + (fixProperties.map { it.name } + mutableProperties.map { it.initial().name })).joinToString(separator = ",")})")
                .build()