package tech.skot.tools.gradle

import com.android.ide.common.util.toPathStringOrNull
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.*
import tech.skot.Versions

open class SKPluginToolsExtension {
    var startScreen: String? = null
    var appPackage: String? = null
    var baseActivity:String? = null
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


        project.task("generate") {

            doLast {
                println("Skot version ${Versions.skot}")
                println("generate .........")
                project.javaexec {
                    main = "tech.skot.tools.generation.GenerateKt"
                    classpath = sourceSet.runtimeClasspath
                    args = listOf(extension.appPackage, extension.startScreen, extension.baseActivity, project.rootDir.toPath().toString())
                }


//                println("ktLint ......")
//                val srcs = "${project.rootDir.toPath().toString()}/**/generated/**/*.kt"
//                project.javaexec {
//                    main = "com.pinterest.ktlint.Main"
//                    classpath = sourceSet.runtimeClasspath
//                    args = listOf("-F",srcs)
//                }
            }
            dependsOn(project.tasks.getByName("compileKotlin"))
            group = "Skot"

        }


    }


    private fun DependencyHandlerScope.dependencies() {
        this.add("implementation", project(":viewcontract"))
        this.add("implementation", project(":modelcontract"))
        this.add("api", "tech.skot:generator:${Versions.skot}")
        this.add("implementation", "com.pinterest:ktlint:0.40.0")
    }

}



