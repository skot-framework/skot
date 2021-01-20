package tech.skot.tools.starter

import java.io.File

class StarterGenerator(private val rootDir: File) {

    fun generateSkeletton() {
        genGitIgnore()
        genBuildSrc()
        genContract()
    }

    fun genGitIgnore() {
        val gitIgnoreFile = rootDir.resolve(".gitignore")
        if (!gitIgnoreFile.exists()) {
            gitIgnoreFile.writeText(gitIgnore)
        }
    }

    fun genBuildSrc() {
        val buildSrcFile = rootDir.resolve("buildSrc")
        val buildSrcGradleFile = buildSrcFile.resolve("build.gradle.kts")
        if (!buildSrcFile.exists()) {
            buildSrcFile.mkdir()
        }
        if (!buildSrcGradleFile.exists()) {
            buildSrcGradleFile.writeText(buildSrcBuildGradle)
        }
    }

    fun genContract() {
        val dirContract = rootDir.resolve("contract")
        if (!dirContract.exists()) {
            dirContract.mkdir()
            val buildGradleFile = dirContract.resolve("build.gradle.kts")

            mutableListOf<String>().apply {
                add("plugins {")
                add("\tkotlin(\"multiplatform\")")
                add("\tid(\"skot-contract\")")
                add("}")
                add("")
                add("dependencies {")
                add("")
                add("}")
            }.joinToString("\n")
                    .let {
                        buildGradleFile.writeText(it)
                    }
        }

    }

}