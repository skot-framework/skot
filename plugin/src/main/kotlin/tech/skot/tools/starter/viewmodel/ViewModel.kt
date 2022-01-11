package tech.skot.tools.starter.viewmodel

import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.viewModel(){
    ModuleGenerator("viewmodel", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Kotlin("multiplatform"))
            plugin(BuildGradleGenerator.Plugin.Id("tech.skot.viewmodel"))
        }

        androidPackage = configuration.appPackage+".viewmodel"


        mainPackage = configuration.appPackage
    }.generate()
    modules.add("viewmodel")
}