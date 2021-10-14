package tech.skot.tools.starter.model

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
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

        val startModel = ClassName("${configuration.appPackage}.di", "startModel")
        FileSpec.builder(startModel.packageName, startModel.simpleName)
            .addFunction(
                FunSpec.builder(startModel.simpleName)
                    .addModifiers(KModifier.SUSPEND)
                    .build()
            )
            .build()
            .writeTo(rootDir.resolve("$name/src/commonMain/kotlin"))

    }.generate()
    modules.add("model")




}