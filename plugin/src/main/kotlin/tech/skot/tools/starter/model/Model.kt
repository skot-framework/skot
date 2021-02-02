package tech.skot.tools.starter.model

import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator

fun StarterGenerator.model(){
    ModuleGenerator("model", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Kotlin("multiplatform"))
            plugin(BuildGradleGenerator.Plugin.Id("skot-model"))
        }

        androidPackage = configuration.appPackage+".model"
        androidPermission("INTERNET")
        androidPermission("ACCESS_NETWORK_STATE")
        androidPermission("ACCESS_WIFI_STATE")
        androidPermission("WRITE_EXTERNAL_STORAGE")
        androidPermission("READ_EXTERNAL_STORAGE")
        androidPermission("WAKE_LOCK")

        mainPackage = configuration.appPackage
    }.generate()
    modules.add("model")
}