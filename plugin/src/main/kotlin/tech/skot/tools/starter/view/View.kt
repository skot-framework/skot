package tech.skot.tools.starter.view

import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.view(){
    ModuleGenerator("view", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Id("skot-viewlegacy"))
            plugin(BuildGradleGenerator.Plugin.Kotlin("android"))

        }
        androidPackage = configuration.appPackage+".view"
        androidSKActivity = true
        justAndroid = true
        mainPackage = configuration.appPackage
    }.generate()
    modules.add("view")
}