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

        project.extensions.findByType(BaseAppModuleExtension::class)?.conf(project)

        project.dependencies {
            dependencies()
        }
    }


    private fun BaseAppModuleExtension.conf(project: Project) {

        sourceSets {
            getByName("main") {
                java.srcDir("src/androidMain/kotlin")
                java.srcDir("generated/androidMain/kotlin")
                skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach<String> {
                    java.srcDir("src/androidMain/kotlin$it")
                    java.srcDir("generated$it/androidMain/kotlin")
                }
                res.srcDir("src/androidMain/res")
                assets.srcDir("src/androidMain/assets")
                manifest.srcFile("src/androidMain/AndroidManifest.xml")
            }
            getByName("androidTest") {
                java.srcDir("src/androidTest/kotlin")
            }
        }

        compileSdkVersion(tech.skot.Versions.android_compileSdk)

        defaultConfig {
            minSdkVersion(tech.skot.Versions.android_minSdk)
            targetSdkVersion(tech.skot.Versions.android_targetSdk)
        }

        packagingOptions {
            exclude("META-INF/*.kotlin_module")
            exclude("META-INF/*")
        }

        lintOptions {
            isAbortOnError = false
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
            targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
        }


    }


    private fun DependencyHandlerScope.dependencies() {
        add("implementation", project(":viewmodel"))
        add("implementation", project(":model"))
        add("implementation", project(":view"))

        add("debugImplementation","com.squareup.leakcanary:leakcanary-android:2.7")

        add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")

    }

}
