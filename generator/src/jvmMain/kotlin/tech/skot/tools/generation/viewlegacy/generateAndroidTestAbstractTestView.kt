package tech.skot.tools.generation.viewlegacy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import tech.skot.tools.generation.AndroidClassNames
import tech.skot.tools.generation.FrameworkClassNames
import tech.skot.tools.generation.Generator
import tech.skot.tools.generation.fileClassBuilder

fun Generator.generateAndroidTestAbstractTestView() {

    val abstractTestScreen = ClassName(packageName = appPackage, "TestView")
    if (!abstractTestScreen.existsAndroidTestInModule(modules.view)) {
        abstractTestScreen.fileClassBuilder(
            listOfNotNull(
                AndroidClassNames.applicationProvider,
                AndroidClassNames.runBlocking,
                initializeView
            )
        ) {
            superclass(FrameworkClassNames.skTestView)
            addModifiers(KModifier.ABSTRACT)

            addProperty(PropertySpec.builder("strings",stringsImpl )
                .initializer("StringsImpl(ApplicationProvider.getApplicationContext())").build())
            addProperty(PropertySpec.builder("icons",iconsImpl )
                .initializer("IconsImpl()").build())

            addFunction(FunSpec.builder("initialize")
                .addAnnotation(AndroidClassNames.Annotations.before)
                .addStatement("runBlocking { initializeView() }")
                .build())
        }.writeTo(androidTestSources(modules.view))
    }


}