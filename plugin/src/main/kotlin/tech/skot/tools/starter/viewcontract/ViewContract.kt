package tech.skot.tools.starter.viewcontract

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
    }.generate()
    modules.add("viewcontract")
}