package tech.skot.tools.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.skot.tools.starter.StarterGenerator

class PluginGeneral:Plugin<Project> {
    override fun apply(project: Project) {
        project.task("initialize") {
            doLast {
                println("------- Generate from Plugin General App essai")
//                StarterGenerator(project.rootDir).generateSkeletton()
            }
        }.group = "SKot"



    }
}