package tech.skot.tools.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions

class PluginLibraryContract : Plugin<Project> {

    override fun apply(project: Project) {


        project.plugins.apply("maven-publish")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf(project)

    }


    private fun KotlinMultiplatformExtension.conf(project: Project) {
        jvm()

        sourceSets["commonMain"].dependencies {
            api("${Versions.group}:viewcontract:${Versions.skot}")
        }

    }

}



