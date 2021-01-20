package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions

//open class SKPluginAppExtension {
//    var message: String? = null
//}

class PluginApp: Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginAppExtension>("skot")
        project.plugins.apply("com.android.application")

        project.extensions.findByType(BaseAppModuleExtension::class)?.conf()

        project.dependencies {
            dependencies()
        }
    }


    private fun BaseAppModuleExtension.conf() {

        sourceSets {
            getByName("main") {
                java.srcDir("src/main/kotlin")
                java.srcDir("generated/main/kotlin")
//                java.srcDir("src/main/kotlin${extra["env"]}")
            }
            getByName("androidTest") {
                java.srcDir("src/androidTest/kotlin")
            }
        }

        compileSdkVersion(Versions.android_compileSdk)

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)
            targetSdkVersion(Versions.android_targetSdk)
        }

        packagingOptions {
            exclude("META-INF/*.kotlin_module")
            exclude("META-INF/*")
        }

        lintOptions {
            isAbortOnError = false
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

    }


    private fun DependencyHandlerScope.dependencies() {
        add("api", project(":viewmodel"))
        add("api", project(":model"))
        add("api", project(":view"))
    }

}
