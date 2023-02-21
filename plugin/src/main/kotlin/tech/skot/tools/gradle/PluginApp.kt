package tech.skot.tools.gradle

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.project
import tech.skot.Versions

//open class SKPluginAppExtension {
//    var message: String? = null
//}

class PluginApp : Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginAppExtension>("skot")
        project.plugins.apply("com.android.application")

        project.extensions.findByType(BaseAppModuleExtension::class)?.conf(project)

        project.dependencies {
            dependencies(project)
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

        androidBaseConfig(project)

        packagingOptions {
            exclude("META-INF/*.kotlin_module")
            exclude("META-INF/*")
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true

        }


    }


    private fun DependencyHandlerScope.dependencies(project: Project) {
        add("implementation", project(":viewmodel"))
        add("implementation", project(":model"))
        add("implementation", project(":view"))

        val androidProperties = project.skReadAndroidProperties()
        if (androidProperties?.leakCanary != false) {
            add("debugImplementation", "com.squareup.leakcanary:leakcanary-android:2.9.1")
        }

        add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.0.2")


    }

}
