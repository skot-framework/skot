package tech.skot.tools.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinCommonCompilation
import org.jetbrains.kotlin.gradle.plugin.sources.getSourceSetsFromAssociatedCompilations
import tech.skot.Versions
import kotlin.reflect.jvm.internal.impl.descriptors.annotations.KotlinTarget

open class SKPluginToolsExtension {
    var startScreen:String? = null
}

class PluginTools : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<SKPluginToolsExtension>("skot")
        project.plugins.apply("java-library")
        project.plugins.apply("kotlin")
//        project.plugins.apply("kotlinx-serialization")

        project.dependencies {
            dependencies()
        }


        val javaPluginConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        val sourceSet = javaPluginConvention.sourceSets["main"]


        var startScreen = "walou"
        val justWait = project.task("JustWait") {

            doLast {
                println("extension.startScreen : -----> ${extension.startScreen}")
                startScreen = extension.startScreen ?: "What ??"
            }
        }
        val bonj = project.task<JavaExec>("bonjour") {
            group = "Skot"
            main = "tech.skot.tools.generation.BonjourKt"
            args = listOf(startScreen)
//            getSourceSetsFromAssociatedCompilations(KotlinCommonCompilation(KotlinTarget.ALL))
            classpath = sourceSet.runtimeClasspath
            dependsOn(justWait)
        }


    }


    private fun DependencyHandlerScope.dependencies() {
        this.add("implementation", project(":viewcontract"))
        this.add("implementation", project(":modelcontract"))
        this.add("api", "tech.skot:generator:${Versions.skot}")
    }

}



