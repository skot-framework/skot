package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import tech.skot.Versions
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


const val SKOT_ANDROID_PROPERIES_FILE_NAME = "skot_android.properties"

fun skReadAndroidProperties(path: Path): SKAndroidProperties? {
    val propertiesPath = path.resolve(SKOT_ANDROID_PROPERIES_FILE_NAME)
    return if (Files.exists(propertiesPath)) {
        val properties = Properties()
        properties.load(FileInputStream(propertiesPath.toFile()))
        SKAndroidProperties(properties)
    } else {
       null
    }
}

class SKAndroidProperties(private val properties: Properties) {
    val minSdk:Int?
        get() = (properties.get("minSdk") as? String)?.toInt()
}

fun Project.skReadAndroidProperties(): SKAndroidProperties? = skReadAndroidProperties(rootProject.rootDir.toPath())

fun TestedExtension.androidBaseConfig(androidProperties: SKAndroidProperties?) {
    defaultConfig {
        minSdk = androidProperties?.minSdk ?: Versions.android_minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        targetSdk = Versions.android_compileSdk
    }

    lintOptions {
        isAbortOnError = false
    }
}

fun LibraryExtension.androidBaseConfig(project: Project) {
    val androidProperties = project.skReadAndroidProperties()
    androidBaseConfig(androidProperties)
    compileSdk = Versions.android_compileSdk
}

fun BaseAppModuleExtension.androidBaseConfig(project: Project) {
    val androidProperties = project.skReadAndroidProperties()
    androidBaseConfig(androidProperties)
    compileSdk = tech.skot.Versions.android_compileSdk
}