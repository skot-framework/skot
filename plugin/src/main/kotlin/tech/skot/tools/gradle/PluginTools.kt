package tech.skot.tools.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import tech.skot.Versions

//open class SKPluginToolsExtension {
//}

class PluginTools : Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginToolsExtension>("skot")
        project.plugins.apply("java-library")
        project.plugins.apply("kotlin")
        project.plugins.apply("kotlinx-serialization")

        project.dependencies {
            dependencies()
        }
    }


    private fun DependencyHandlerScope.dependencies() {
        add("implementation", "tech.skot:generator:${Versions.skot}")
//        add("implementation", project(":contract", "jvmDefault"))
        add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
//        add("implementation", "com.squareup:kotlinpoet:${Versions.kotlinpoet}")

        add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
        add("implementation", "io.ktor:ktor-server-netty:${Versions.ktor}")
        add("implementation", "io.ktor:ktor-client-apache:${Versions.ktor}")
        add("implementation", "io.ktor:ktor-client-json-jvm:${Versions.ktor}")
        add("implementation", "io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")
    }

}



