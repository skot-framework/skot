package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions

//open class SKPluginViewContractExtension {
//}

class PluginViewContract : Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginContractExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("maven-publish")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(LibraryExtension::class)?.conf()

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf()

    }


    private fun LibraryExtension.conf() {

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)
        }
        compileSdkVersion(Versions.android_compileSdk)

        sourceSets {
            getByName("main").java.srcDirs("generated/androidMain/kotlin")
            getByName("main").java.srcDirs("src/androidMain/kotlin")
            getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
            getByName("main").res.srcDir("src/androidMain/res")
        }

        lintOptions {
            isAbortOnError = false
        }

    }

    private fun KotlinMultiplatformExtension.conf() {
        jvm("jvm")
        android("android")

//        sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
//        sourceSets["commonMain"].dependencies {
////            api("tech.skot:contract:${Versions.skot}")
//            api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
//            api("com.soywiz.korlibs.klock:klock:${Versions.klock}")
//        }
//
//        sourceSets["jvmMain"].dependencies {
////            api("tech.skot:contract-jvm:${Versions.skot}")
//        }
//        sourceSets["androidMain"].dependencies {
////            api("tech.skot:contract-jvm:${Versions.skot}")
//            implementation("tech.skot:core-android:${Versions.skot}")
//        }
    }

}



