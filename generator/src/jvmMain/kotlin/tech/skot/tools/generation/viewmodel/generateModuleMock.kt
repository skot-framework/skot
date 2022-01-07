package tech.skot.tools.generation.viewmodel

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import tech.skot.tools.generation.FrameworkClassNames.coreViewInjector
import tech.skot.tools.generation.FrameworkClassNames.coreViewInjectorMock
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.addImportClassName

fun Generator.generateModuleMock() {
    FileSpec.builder(viewModelModuleMock.packageName, viewModelModuleMock.simpleName)
        .addProperty(
            PropertySpec.builder(
                viewModelModuleMock.simpleName,
                module.parameterizedBy(mockInjector)
            )
                .initializer(
                    CodeBlock.builder()
                        .beginControlFlow("module")
                        .addStatement("single<${coreViewInjector.simpleName}> { ${coreViewInjectorMock.simpleName}() }")
                        .addStatement("single<${stringsInterface.simpleName}> { ${stringsMock.simpleName}()}")
                        .addStatement("single<${stringsInterface.simpleName}> { ${stringsMock.simpleName}()}")
                        .addStatement("single<${pluralsInterface.simpleName}> { ${pluralsMock.simpleName}()}")
                        .addStatement("single<${iconsInterface.simpleName}> { ${iconsMock.simpleName}()}")
                        .addStatement("single<${colorsInterface.simpleName}> { ${colorsMock.simpleName}()}")
                        .addStatement("single<${stylesInterface.simpleName}> { ${stylesMock.simpleName}()}")
                        .addStatement("single<${viewInjectorInterface.simpleName}> { ${viewInjectorMock.simpleName}()}")
                        .addStatement("single<${modelInjectorInterface.simpleName}> { ${modelInjectorMock.simpleName}()}")
                        .addStatement("single<${transitionsInterface.simpleName}> { ${transitionsMock.simpleName}()}")
                        .endControlFlow()
                        .build()
                )
                .build()

        )
        .addImportClassName(coreViewInjector)
        .addImportClassName(coreViewInjectorMock)
        .addImportClassName(moduleFun)
        .addImportClassName(baseInjector)
        .addImportClassName(stringsMock)
        .addImportClassName(stringsInterface)
        .addImportClassName(pluralsMock)
        .addImportClassName(pluralsInterface)
        .addImportClassName(iconsMock)
        .addImportClassName(iconsInterface)
        .addImportClassName(colorsInterface)
        .addImportClassName(colorsMock)
        .addImportClassName(stylesInterface)
        .addImportClassName(stylesMock)
        .addImportClassName(transitionsInterface)
        .addImportClassName(transitionsMock)
        .build()
        .writeTo(generatedJvmTestSources(modules.viewmodel))
}