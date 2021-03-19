package tech.skot.tools.starter.viewcontract

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.viewContract(){
    ModuleGenerator("viewcontract", configuration, rootDir).apply {
        buildGradle {
            publish(group = "\"${configuration.appPackage}\"", version = "Build.versionName")
            plugin(BuildGradleGenerator.Plugin.Kotlin("multiplatform"))
            plugin(BuildGradleGenerator.Plugin.Id("skot-viewcontract"))
        }
        androidPackage = configuration.appPackage+".viewcontract"
        mainPackage = configuration.appPackage


        val splashVC = ClassName("${configuration.appPackage}.screens", "SplashVC")
        FileSpec.builder(splashVC.packageName, splashVC.simpleName)
                .addType(
                        TypeSpec.interfaceBuilder(splashVC.simpleName)
                                .addSuperinterface(ClassName("tech.skot.core.components", "ScreenVC"))
                                .build()
                )
                .build()
                .writeTo(rootDir.resolve("$name/src/commonMain/kotlin"))

    }.generate()
    modules.add("viewcontract")
}