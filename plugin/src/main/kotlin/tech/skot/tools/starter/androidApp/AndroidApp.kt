package tech.skot.tools.starter.androidApp

import tech.skot.tools.starter.BuildGradleGenerator
import tech.skot.tools.starter.ModuleGenerator
import tech.skot.tools.starter.StarterGenerator


const val startGradleAndroidBlock = """android {

    defaultConfig {
        applicationId = "com.sezane.android"
        
        versionCode = Build.versionCode
        versionName = Build.versionName
    }

//    signingConfigs {
//        create("release") {
//            val signingProps = Build.readSigningProperties("${'$'}projectDir")
//            keyAlias = signingProps.getProperty("key.alias")
//            keyPassword = signingProps.getProperty("key.password")
//            storeFile = file("signing/keystore.jks")
//            storePassword = signingProps.getProperty("store.password")
//        }
//    }


    buildTypes {
        getByName("debug") {
            applicationIdSuffix = "dev"
            manifestPlaceholders["app_name"] = "Dev ${'$'}{Build.appName}"
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

//            signingConfig = signingConfigs.getByName("release")

            manifestPlaceholders["app_name"] = Build.appName"
        }
    }

}"""

fun StarterGenerator.androidApp(){
    ModuleGenerator("androidApp", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Id("skot-app"))
            plugin(BuildGradleGenerator.Plugin.Kotlin("android"))

            androidBlock = startGradleAndroidBlock

        }
        androidPackage = configuration.appPackage+".android"
        androidSKActivity = true




    }.generate()
    modules.add("androidApp")
}