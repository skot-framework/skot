package tech.skot.tools.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import tech.skot.Versions
import tech.skot.tools.starter.StarterConfiguration
import tech.skot.tools.starter.StarterGenerator


open class SKPluginStarterExtension {
    var appPackage: String? = null
    var appName: String? = null
}

class PluginStarter : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<SKPluginStarterExtension>("skot")
        project.task("start") {
            doLast {


                val appPackage = extension.appPackage
                val appName = extension.appName
                if (appPackage == null ||appName == null) {
                    println("Attention !!! Merci de renseigner le package de l'application ainsi que son nom")
                } else {
                    println("Skot version ${Versions.skot}")
                    println("génération du squelette d'application")

                    StarterGenerator(project.rootProject.rootDir.toPath(), StarterConfiguration(
                            appName = appName,
                            appPackage = appPackage
                    )).generateSkeletton()
                }

            }
        }.group = "SKot"


        project.task("version") {
            println("Skot version ${Versions.skot}")
        }
    }
}