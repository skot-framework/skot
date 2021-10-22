package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions
import tech.skot.tools.gradle.SKLibrary.Companion.addDependenciesToLibraries

//open class SKPluginViewContractExtension {
//}

class PluginViewContract : Plugin<Project> {

    override fun apply(project: Project) {


//        val extension = project.extensions.create<SKPluginContractExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("maven-publish")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(LibraryExtension::class)?.conf()

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf(project)


//        project.task("TestGen") {
//            classpat
//            doLast {
//                println("----------###SK###--------- coucou")
//                Class.forName("tech.skot.essai.SplashVC")
//                val classss = Class.forName("tech.skot.essai.SplashVC")
//                println(classss)
//            }
//            group = "skot"
//        }
    }


    private fun LibraryExtension.conf() {

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)
        }
        compileSdkVersion(Versions.android_compileSdk)

        sourceSets {
            getByName("main").java.srcDirs("generated/androidMain/kotlin")
            getByName("main").java.srcDirs("src/androidMain/kotlin")
            getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
            getByName("main").res.srcDir("src/androidMain/res")
        }

        lintOptions {
            isAbortOnError = false
        }

    }

    private fun KotlinMultiplatformExtension.conf(project: Project) {
        jvm("jvm")
        android("android")
        if (project.hasIosApp()) {
            ios {
                binaries {
                    framework {
                        baseName = "viewcontract"
                    }
                }
            }
        }


        sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
        sourceSets["commonMain"].dependencies {
            api("${Versions.group}:viewcontract:${Versions.skot}")
        }

        println("Adding dependencies to libraries ")
        addDependenciesToLibraries(
            this,
            (project.parent?.projectDir ?: project.rootDir).toPath(),
            "viewcontract"
        )

        skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach {
            sourceSets["commonMain"].kotlin.srcDir("src/commonMain/kotlin$it")
            sourceSets["androidMain"].kotlin.srcDir("src/androidMain/kotlin$it")
            sourceSets["commonMain"].kotlin.srcDir("generated$it/commonMain/kotlin")
            sourceSets["androidMain"].kotlin.srcDir("generated$it/androidMain/kotlin")
        }

    }

}



