package tech.skot.tools.generation

import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
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
            variants = properties.getProperty("variants").split(",").filter { it.isNotBlank() } ?: emptyList(),
            env = properties.getProperty("environment").let { if (it.isBlank()) null else it }
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
        .map { it.capitalizeAsciiOnly() }
        .combinaisons()
        .drop(1)