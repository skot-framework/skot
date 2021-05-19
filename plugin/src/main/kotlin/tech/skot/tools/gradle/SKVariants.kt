package tech.skot.tools.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

data class SKVariants(val variants: List<String>, val env: String?) {
    fun toList(): List<String> = if (env != null) {
        variants + env
    } else {
        variants
    }
}

fun skReadVariants(path: Path): SKVariants {
    val propertiesPath = path.resolve("skot_variants.properties")
    return if (Files.exists(propertiesPath)) {
        val properties = Properties()
        properties.load(FileInputStream(propertiesPath.toFile()))
        SKVariants(
            variants = properties.getProperty("variants").split(",") ?: emptyList(),
            env = properties.getProperty("environment")
        )
    } else {
        SKVariants(
            variants = emptyList(),
            env = null
        )
    }

}

fun List<String>.combinaisons(): List<String> {
    return when (size) {
        0 -> {
            emptyList<String>()
        }
        1 -> {
            listOf("", first())
        }
        else -> {
            drop(1).combinaisons().flatMap {
                listOf("" + it, first() + it)
            }
        }
    }
}

fun skVariantsCombinaison(path: Path) =
    skReadVariants(path)
        .toList()
        .map { it.capitalize() }
        .combinaisons()
        .drop(1)

fun Project.skVariantsCombinaison() = skVariantsCombinaison(rootProject.rootDir.toPath())

fun Project.skReadVariants(): SKVariants = skReadVariants(rootProject.rootDir.toPath())

fun Project.skBuildSrcVariants(sourceSets: SourceSetContainer) {
    sourceSets.apply {
        getByName("main") {
            java {
                skVariantsCombinaison(rootDir.parentFile.toPath()).forEach {
                    srcDir("src/main$it/kotlin")
                }
            }
        }
    }
}
