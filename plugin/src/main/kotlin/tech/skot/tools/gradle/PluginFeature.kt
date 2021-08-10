package tech.skot.tools.gradle

import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.extension.DynamicFeatureAndroidComponentsExtension
import com.android.build.gradle.AppExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature
import tech.skot.Versions


class PluginFeature : Plugin<Project> {

    override fun apply(project: Project) {
//        project.plugins.apply("com.android.library")
        project.plugins.apply("com.android.dynamic-feature")
        println("############## project.extensions ${project.extensions}")
        println("############## applying skot-feature !!!")
        project.extensions.findByType(DynamicFeatureExtension::class)?.android(project)
        project.extensions.findByType(AppExtension::class)
            ?.android(project)

        project.dependencies {
            dependencies(project)
        }
    }


    private fun DynamicFeatureExtension.android(project: Project) {

        println("############## applying skot-feature LibraryExtension !!!")
        sourceSets {
            getByName("main") {
                java.srcDir("src/androidMain/kotlin")
                java.srcDir("generated/androidMain/kotlin")
//                java.srcDir("src/main/kotlin${extra["env"]}")
                res.srcDir("src/androidMain/res")
                res.srcDir("src/androidMain/res_referenced")

                manifest.srcFile("src/androidMain/AndroidManifest.xml")
            }
            getByName("androidTest") {
                java.srcDir("src/androidTest/kotlin")
            }
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

        buildFeatures {
            viewBinding = true
        }

    }

    private fun AppExtension.android(project: Project) {
        compileSdkVersion(Versions.android_compileSdk)

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)
            targetSdkVersion(Versions.android_targetSdk)
        }
    }


    private fun DependencyHandlerScope.dependencies(project: Project) {

        val parentProjectPath = project.parent?.path ?: ""

        add("implementation", project("$parentProjectPath:viewmodel"))
        add("implementation", project("$parentProjectPath:model"))
//        add("implementation", project("$parentProjectPath:view"))
        add("api", "tech.skot:viewlegacy:${Versions.skot}")
        add("api", project("$parentProjectPath:viewcontract"))

        add("implementation", project(":androidApp"))
//        add("debugImplementation","com.squareup.leakcanary:leakcanary-android:2.6")
    }

}
