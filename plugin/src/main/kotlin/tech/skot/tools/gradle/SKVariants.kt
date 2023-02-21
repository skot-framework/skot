package tech.skot.tools.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.name

data class SKVariants(val variants: List<String>, val env: String?) {
    fun toList(): List<String> = if (env != null) {
        variants + env
    } else {
        variants
    }
}

const val SKOT_VARIANT_PROPERIES_FILE_NAME = "skot_variants.properties"

fun skReadVariants(path: Path): SKVariants {
    val propertiesPath = path.resolve(SKOT_VARIANT_PROPERIES_FILE_NAME)
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
                    println("#######will add build src set : $it")
                    srcDir("src/main/kotlin$it")
                }
            }
        }
    }
}

fun Project.skSwitchTask(name:String, environment:String, vararg variant:String) {
    task(name) {
        doFirst {
            println("Switch to Variant environment=$environment  variants=${variant.joinToString(" ")} ")
            val rootPath = rootProject.rootDir.toPath()
            val builSrcRep = rootPath.resolve("buildSrc/build")
            val propertiesPath = rootPath.resolve(SKOT_VARIANT_PROPERIES_FILE_NAME)
            Files.write(propertiesPath, listOf(
                "variants=${variant.joinToString(",")}",
                "environment=$environment"
            ))
            if (Files.exists(builSrcRep)) {
                println("Will delete buildSrc/build directory")
                builSrcRep.toFile().deleteRecursively()
            }
        }

        group = "skot_switch_variant"
    }
}