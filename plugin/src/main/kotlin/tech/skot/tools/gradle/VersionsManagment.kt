package tech.skot.tools.gradle

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import tech.skot.tools.generation.writeStringTo
import java.nio.file.Files

fun Project.skVersionCodePropertiesPath() = rootProject.rootDir.toPath().resolve("skot_version_code.properties")

fun Project.skVersionCode():Int =
    Files.readAllLines(skVersionCodePropertiesPath()).first().toInt()

fun Project.skSetVersionCode(newVersionCode:Int) {
    Files.writeString(skVersionCodePropertiesPath(), newVersionCode.toString())
}

@Serializable
data class UploadedInfos(val commit:String?, val buildNumber:Int)
val json by lazy {
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}

private fun Project.skComputeVersionCodeAndReleaseNote(branch:String, nbMaxCommitsInReleaseNote:Int) {
    task("compute_version_code_and_release_note") {
        doFirst {
            println("setting version Code from server")
            println("--fetch last uploaded versionCode")
            val lastUploadedInfos:UploadedInfos? =
                try {
                    val strInfos = commandLine("scripts/versions/getLastUploadedInfos.sh")
//                    val tab = strInfos.split("_")
//                    UploadedInfos(tab[0], tab[1].toInt())
                    json.decodeFromString(UploadedInfos.serializer(), strInfos)
                }
                catch (exception:kotlin.Exception) {
                    null
                }
            println("--fetched last uploaded infos = $lastUploadedInfos")
            val currentVersionCode = skVersionCode()
            if (lastUploadedInfos != null) {
                skSetVersionCode(Math.max(lastUploadedInfos.buildNumber, currentVersionCode + 1))
                val lastCommitHashes = commandLine("git", "show", "-s", "--format=%h", branch, "-$nbMaxCommitsInReleaseNote").split("\n").filter { it.isNotBlank() }
                val lastUploadCommitIndex = lastCommitHashes.indexOf(lastUploadedInfos.commit)
                val nbCommitsInNote = if (lastUploadCommitIndex != -1) {
                    lastUploadCommitIndex
                } else {
                    Math.min(nbMaxCommitsInReleaseNote, lastCommitHashes.size)
                }
                val noteContent = commandLine("git", "show", "-s", "--format=%m%s%n%b%n", "-$nbCommitsInNote")
                rootDir.toPath().writeStringTo("androidApp/distribution/release-note.txt", noteContent, true)

            }
        }
        group = "skot_versions"
    }
}

private fun Project.skSaveUploadedInfos(branch:String) {
    task("save_uploaded_versions_infos") {
        doFirst {
            val versionCode = skVersionCode()
            val lastCommitHash = commandLine("git", "show", "-s", "--format=%h", branch).substringBefore("\n")
            commandLine("scripts/versions/saveLastUploadedInfos.sh", lastCommitHash)
        }
        group = "skot_versions"
    }
}

fun Project.skVersionsTasks(branchEnvVariable:String, defaultBranch:String, nbMaxCommitsInReleaseNote:Int) {
    val branch = System.getenv(branchEnvVariable) ?: defaultBranch
    skComputeVersionCodeAndReleaseNote(branch, nbMaxCommitsInReleaseNote)
    skSaveUploadedInfos(branch)
}