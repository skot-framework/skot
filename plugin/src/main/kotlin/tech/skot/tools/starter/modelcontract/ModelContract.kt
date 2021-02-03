package tech.skot.tools.starter.modelcontract

import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.modelContract(){
    ModuleGenerator("modelcontract", configuration, rootDir).apply {
        buildGradle {
            publish(group = "\"${configuration.appPackage}\"", version = "Build.versionName")
            plugin(BuildGradleGenerator.Plugin.Kotlin("multiplatform"))
            plugin(BuildGradleGenerator.Plugin.Id("skot-modelcontract"))
        }

        androidPackage = configuration.appPackage+".modelcontract"
        androidString("appName", configuration.appName)

        mainPackage = configuration.appPackage
    }.generate()
    modules.add("modelcontract")
}