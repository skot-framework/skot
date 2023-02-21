package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.model.KotlinAndroidExtension
import tech.skot.Versions

//open class SKPluginLibraryViewLegacyExtension {
//}

class PluginLibraryViewLegacy: Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginLibraryViewLegacyExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("maven-publish")

//        project.plugins.apply("com.github.ben-manes.versions")

        project.extensions.findByType(LibraryExtension::class)?.android(project)

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf()

        project.dependencies {
            dependencies(project)
        }

    }


    private fun LibraryExtension.android(project: Project) {

        sourceSets.getByName("main") {
            java.srcDir("src/androidMain/kotlin")
            res.srcDir("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }

        sourceSets.getByName("androidTest") {
            res.srcDirs("src/androidTest/res")
        }

        androidBaseConfig(project)
        

        buildFeatures {
            viewBinding = true
        }



    }

    private fun KotlinMultiplatformExtension.conf() {
       android() {
            publishLibraryVariants("release", "debug")
            publishLibraryVariantsGroupedByFlavor = true
        }
    }


    private fun DependencyHandlerScope.dependencies(project: Project) {
        add("implementation", "${Versions.group}:viewlegacy:${Versions.skot}")
        if (project.findProject(":viewcontract") != null) {
            add("implementation", project(":viewcontract"))
        }

        add("androidTestImplementation", "${Versions.group}:viewlegacyTests:${Versions.skot}")
    }

}
