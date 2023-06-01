package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions

//open class SKPluginLibraryExtension {
//    var message: String? = null
//}

@Suppress("UnstableApiUsage")
class PluginLibrary: Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginLibraryExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("maven-publish")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(LibraryExtension::class)?.conf()

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf()

    }


    private fun LibraryExtension.conf() {

        defaultConfig {
            minSdk = Versions.android_minSdk
        }
        compileSdk = Versions.android_compileSdk

        sourceSets {
            getByName("main").java.srcDirs("src/androidMain/kotlin")
            getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
            getByName("main").res.srcDir("src/androidMain/res")
            getByName("test").java.srcDirs("src/javaTest/kotlin")
        }
    }

    private fun KotlinMultiplatformExtension.conf() {

        jvm()

        //ios()

        android {
                compilations.all {
                    kotlinOptions {
                        jvmTarget = "1.8"
                    }
                }
            publishLibraryVariants("release")
        }

        sourceSets["commonMain"].dependencies {
            api(project(":viewcontract"))
        }


        sourceSets["commonMain"].dependencies {
            implementation("${Versions.group}:viewmodel:${Versions.skot}")
        }


    }


}
