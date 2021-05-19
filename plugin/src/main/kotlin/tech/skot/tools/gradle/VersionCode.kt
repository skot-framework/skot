package tech.skot.tools.gradle

import org.gradle.api.Project
import java.nio.file.Files

fun Project.skVersionCode():Int =
    Files.readAllLines(rootProject.rootDir.toPath().resolve("skot_version_code.properties")).first().toInt()
