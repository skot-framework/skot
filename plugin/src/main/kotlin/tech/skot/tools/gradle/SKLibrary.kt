package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import tech.skot.Versions
import java.nio.file.Files
import java.nio.file.Path


data class SKLibrary(val group: String, val version: String, val viewModule:String) {

    companion object {
        fun getDeclaredLibraries(path: Path): List<SKLibrary> {
            println("-------- getDeclaredLibraries   ${path.toString()}")
            return Files.readAllLines(path.resolve("skot_librairies.properties"))
                    .filterNot {
                        it.startsWith("//")
                    }
                    .map { line ->
                        println("line: $line")
                        line.substringBefore(",").split(":")
                                .let {
                                    try {
                                        val viewModule = if (it.getOrNull(2).isNullOrBlank()) "viewlegacy" else it[2]
                                        SKLibrary(it[0], it[1], viewModule)
                                    } catch (ex: Exception) {
                                        throw IllegalArgumentException("The library declaration \"$line\" is not wel formated")
                                    }
                                }
                    }
        }

        fun addDependenciesToLibraries(kotlinExtension:KotlinMultiplatformExtension, path:Path, sourcesSet:String = "commonMain", module:String) {
            kotlinExtension.addDependenciesToLibraries(
                    getDeclaredLibraries(path), sourcesSet, module
            )
        }

        fun KotlinMultiplatformExtension.addDependenciesToLibraries(libraries: List<SKLibrary>, sourcesSet:String, module:String) {
            sourceSets[sourcesSet].dependencies {
                libraries.forEach { skApi(it,module) }
            }
        }

        fun KotlinDependencyHandler.skApi(library: SKLibrary, module:String) = api("${library.group}:$module:${library.version}")

        fun addDependenciesToViewLegacy(dependenciesHandler: DependencyHandlerScope, path: Path) {
            getDeclaredLibraries(path)
                    .forEach {
                        dependenciesHandler.skApiViewLegacy(it)
                    }
        }


        fun DependencyHandlerScope.skApiViewLegacy(library: SKLibrary) = add("api", "${library.group}:${library.viewModule}:${library.version}")

    }


}
