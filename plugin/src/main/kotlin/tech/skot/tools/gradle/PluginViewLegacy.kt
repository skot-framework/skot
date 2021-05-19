package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import tech.skot.Versions
import tech.skot.tools.gradle.SKLibrary.Companion.addDependenciesToViewContract

//open class SKPluginViewLegacyExtension {
//}

class PluginViewLegacy: Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginViewLegacyExtension>("skot")
        project.plugins.apply("com.android.library")
//        project.plugins.apply("com.github.ben-manes.versions")

        project.extensions.findByType(LibraryExtension::class)?.android(project)

        project.dependencies {
            dependencies()
            addDependenciesToViewContract(this, project.rootDir.toPath())
        }

    }


    private fun LibraryExtension.android(project: Project) {

        sourceSets.getByName("main") {
            java.srcDir("src/androidMain/kotlin")
            java.srcDir("generated/androidMain/kotlin")
            res.srcDir("src/androidMain/res")
            res.srcDir("src/androidMain/res_referenced")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")

            skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach {
                res.srcDir("src/androidMain/res$it")
                java.srcDir("src/androidMain$it/kotlin")
            }
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




    private fun DependencyHandlerScope.dependencies() {
        add("api", "tech.skot:viewlegacy:${Versions.skot}")
        add("api", project(":viewcontract"))
    }

}
