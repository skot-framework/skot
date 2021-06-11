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

        project.extensions.findByType(LibraryExtension::class)?.android()

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf()

        project.dependencies {
            dependencies()
        }

    }


    private fun LibraryExtension.android() {

        sourceSets.getByName("main") {
            java.srcDir("src/androidMain/kotlin")
            res.srcDir("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)
        }
        compileSdkVersion(Versions.android_compileSdk)

        lintOptions {
            isAbortOnError = false
        }

        buildFeatures {
            viewBinding = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }


    }

    private fun KotlinMultiplatformExtension.conf() {
       android("android") {
            publishLibraryVariants("release", "debug")
            publishLibraryVariantsGroupedByFlavor = true
        }
    }


    private fun DependencyHandlerScope.dependencies() {
        add("implementation", "tech.skot:viewlegacy:${Versions.skot}")
        add("implementation", project(":viewmodel"))
    }

}
