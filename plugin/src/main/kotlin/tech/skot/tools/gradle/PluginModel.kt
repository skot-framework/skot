package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
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

        project.extensions.findByType(LibraryExtension::class)?.conf()

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf(project)


    }


    private fun LibraryExtension.conf() {

        defaultConfig {
            minSdkVersion(Versions.android_minSdk)

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            testInstrumentationRunnerArguments["clearPackageData"] = "true"

            testOptions {
                execution = "ANDROIDX_TEST_ORCHESTRATOR"
            }
        }

        compileSdkVersion(Versions.android_compileSdk)

        sourceSets {
            getByName("main").java.srcDirs("src/androidMain/kotlin")
            getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")

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
        android("android")

        sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")
        sourceSets["commonMain"].dependencies {
            implementation(project(":modelcontract"))
            api("tech.skot:model:${Versions.skot}")
            api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
            implementation("io.ktor:ktor-client-json:${Versions.ktor}")
            implementation("io.ktor:ktor-client-serialization:${Versions.ktor}")
            implementation("io.ktor:ktor-client-logging:${Versions.ktor}")
            implementation("io.ktor:ktor-client-auth:${Versions.ktor}")

        }

        skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach {
            sourceSets["commonMain"].kotlin.srcDir("src/commonMain/kotlin$it")
            sourceSets["androidMain"].kotlin.srcDir("src/androidMain/kotlin$it")
        }

        sourceSets["commonTest"].kotlin.srcDir("src/commonTest/kotlin")

        sourceSets["commonTest"].dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}")
            implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}")
        }

        sourceSets["androidTest"].dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            implementation("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")
        }


        sourceSets["androidMain"].kotlin.srcDir("generated/androidMain/kotlin")
        sourceSets["androidMain"].dependencies {
            implementation("io.ktor:ktor-client-android:${Versions.ktor}")
            implementation("io.ktor:ktor-client-json-jvm:${Versions.ktor}")
            implementation("io.ktor:ktor-client-serialization-jvm:${Versions.ktor}")
            implementation("io.ktor:ktor-client-logging-jvm:${Versions.ktor}")
            implementation("io.ktor:ktor-client-auth-jvm:${Versions.ktor}")
            implementation("org.slf4j:slf4j-simple:${Versions.sl4j}")
        }

    }


}
