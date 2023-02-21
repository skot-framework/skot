package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions

//open class SKPluginModelExtension {
//    var message: String? = null
//}

class PluginModel: Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginContractExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(LibraryExtension::class)?.conf(project)

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf(project)

    }


    private fun LibraryExtension.conf(project:Project) {

        androidBaseConfig(project)


        sourceSets {
            getByName("main"){
                java.srcDirs("src/androidMain/kotlin")
                res.srcDir("src/androidMain/res")
                manifest.srcFile("src/androidMain/AndroidManifest.xml")

                skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach {
                    res.srcDir("src/androidMain/res$it")
                    java.srcDir("src/androidMain/kotlin$it")
                }
            }

            getByName("androidTest") {
                java.srcDir("src/androidTest/kotlin")
            }


        }

        packagingOptions {
            exclude("META-INF/*.kotlin_module")
            exclude("META-INF/*")
        }



    }

    private fun KotlinMultiplatformExtension.conf(project: Project) {
        android()
        jvm()



        sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")



        val parentProjectPath = project.parent?.path ?: ""

        sourceSets["commonMain"].dependencies {
            api(project("$parentProjectPath:modelcontract"))
            api("${Versions.group}:model:${Versions.skot}")
            api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")


            if (project.skReadImportsProperties()?.ktor2 != false) {
                api("io.ktor:ktor-client-content-negotiation:${Versions.ktor}")
                api("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")
            }
            else {
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
            }
        }

        skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach {
            sourceSets["commonMain"].kotlin.srcDir("src/commonMain/kotlin$it")
            sourceSets["androidMain"].kotlin.srcDir("src/androidMain/kotlin$it")
        }


        sourceSets["jvmTest"].kotlin.srcDir("generated/jvmTest")

        sourceSets["androidTest"].dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            implementation("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")
        }


        sourceSets["androidMain"].kotlin.srcDir("generated/androidMain/kotlin")

    }


}
