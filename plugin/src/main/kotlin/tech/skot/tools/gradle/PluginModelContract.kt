package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.crash.afterEvaluate
import com.squareup.kotlinpoet.asTypeName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

open class SKPluginModelContractExtension {
    var buildFiles: List<Any>? = null
}

class PluginModelContract : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<SKPluginModelContractExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("maven-publish")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(LibraryExtension::class)?.conf(project, extension)

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf()


        project.afterEvaluate {
            project.tasks.getByName("preDebugBuild").doFirst {
                extension.buildFiles?.let {
                    it.forEach {
                        copyBuildFileToImplementation(it, project, true, true)
                    }
                }
            }
            project.tasks.getByName("preReleaseBuild").doFirst {
                extension.buildFiles?.let {
                    it.forEach {
                        copyBuildFileToImplementation(it, project, true, false)
                    }
                }
            }
        }




    }


    private fun LibraryExtension.conf(project:Project, extension: SKPluginModelContractExtension) {

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)
        }
        compileSdkVersion(Versions.android_compileSdk)


        sourceSets {
            getByName("main").java.srcDirs("generated/androidMain/kotlin")
            getByName("main").java.srcDirs("generated/commonMain/kotlin")
            getByName("main").java.srcDirs("src/androidMain/kotlin")
            getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
            getByName("main").res.srcDir("src/androidMain/res")
        }

//        buildTypes {
//            getByName("debug") {
//
//                project.tasks.getByName("preBuild").dependsOn(project.tasks.create("setDebugOn") {
//                    doFirst {
//
//                        println("----- setting to true")
//                        skDebugMode = true
//                    }
//                })
//            }
//
//            getByName("release") {
//                project.tasks.getByName("preBuild").dependsOn(project.tasks.create("setDebugOff") {
//                    doFirst {
//                        println("----- setting to false")
//                        skDebugMode = false
//                    }
//                })
//
//            }
//        }


        lintOptions {
            isAbortOnError = false
        }

    }

    private fun KotlinMultiplatformExtension.conf() {
        jvm("jvm")
        android("android")

        sourceSets["commonMain"].dependencies {
            api("tech.skot:modelcontract:${Versions.skot}")
        }
    }

}



