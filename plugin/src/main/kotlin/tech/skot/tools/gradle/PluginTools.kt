package tech.skot.tools.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import tech.skot.Versions
import tech.skot.language.Screen
import tech.skot.tools.starter.StarterGenerator

open class SKPluginToolsExtension {
    var startScreen: Screen? = null
    var appName:String? = null
}

class PluginTools : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<SKPluginToolsExtension>("skot")
        project.plugins.apply("java-library")
        project.plugins.apply("kotlin")
        project.plugins.apply("kotlinx-serialization")

        project.dependencies {
            dependencies()
        }

//        project.task("GenerateProjectSkeletton") {
//            doLast {
//                StarterGenerator(project.rootProject.rootDir).generateSkeletton()
//            }
//        }.group = "SKot"

        project.task("generate") {
            doLast {
                println("------- App named : ${extension.appName}")
                extension.startScreen?.let { println("--------------->  ${it::class.simpleName}") }
            }
//            project.gradle.projectsEvaluated {
//                println("------- App named : ${extension.appName}")
//            }

//            val ext = project.extensions.getByName("skot") as SKPluginToolsExtension
//            ext.startScreen?.let { println("--------------->  ${it::class.simpleName}") }
        }.group = "SKot"
    }


    private fun DependencyHandlerScope.dependencies() {
        add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")

        add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
        add("implementation", "io.ktor:ktor-server-netty:${Versions.ktor}")
        add("implementation", "io.ktor:ktor-client-apache:${Versions.ktor}")
        add("implementation", "io.ktor:ktor-client-json-jvm:${Versions.ktor}")
        add("implementation", "io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")
    }

}



