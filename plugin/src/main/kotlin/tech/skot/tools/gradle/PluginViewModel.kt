package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.skot.Versions
import kotlin.reflect.KClass

//open class SKPluginViewModelExtension {
//    var message: String? = null
//}

class PluginViewModel: Plugin<Project> {

    override fun apply(project: Project) {
//        val extension = project.extensions.create<SKPluginViewModelExtension>("skot")
        project.plugins.apply("com.android.library")
        project.plugins.apply("kotlinx-serialization")

        project.extensions.findByType(LibraryExtension::class)?.conf(project)

        project.extensions.findByType(KotlinMultiplatformExtension::class)?.conf(project)

    }


    private fun LibraryExtension.conf(project:Project) {

        androidBaseConfig(project)

        sourceSets {
            getByName("main").java.srcDirs("src/androidMain/kotlin")
            getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
            getByName("test").java.srcDirs("src/javaTest/kotlin")
        }

    }

    private fun KotlinMultiplatformExtension.conf(project: Project) {
        android()
        jvm()


        sourceSets["commonMain"].kotlin.srcDir("generated/commonMain/kotlin")

        val parentProjectPath = project.parent?.path ?: ""


        sourceSets["commonMain"].dependencies {
            api(project("$parentProjectPath:viewcontract"))
            api(project("$parentProjectPath:modelcontract"))
            api("${Versions.group}:core:${Versions.skot}")
            api("${Versions.group}:viewmodel:${Versions.skot}")
        }

        sourceSets["androidMain"].dependencies {
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

        sourceSets["jvmTest"].kotlin.srcDir("generated/jvmTest/kotlin")
        skVariantsCombinaison(project.rootProject.rootDir.toPath()).forEach {
            sourceSets["jvmTest"].kotlin.srcDir("generated$it/jvmTest/kotlin")
        }

        sourceSets["jvmTest"].dependencies {
            implementation("${Versions.group}:viewmodelTests:${Versions.skot}")
        }

        sourceSets["androidTest"].dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
            implementation("org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}")
        }

        SKLibrary.addDependenciesToLibraries(this, (project.parent?.projectDir ?: project.rootDir).toPath(), "commonMain", "viewmodel")
        SKLibrary.addDependenciesToLibraries(this, (project.parent?.projectDir ?: project.rootDir).toPath(), "jvmTest", "viewmodelTests")
    }


}
