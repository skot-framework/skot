package tech.skot.tools.gradle

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import tech.skot.tools.generation.writeStringTo
import java.nio.file.Files
import kotlin.math.max

fun Project.skVersionCodePropertiesPath() = rootProject.rootDir.toPath().resolve("skot_version_code.properties")

fun Project.skVersionCode():Int =
    Files.readAllLines(skVersionCodePropertiesPath()).first().toInt()

fun Project.skSetVersionCode(newVersionCode:Int) {
    Files.writeString(skVersionCodePropertiesPath(), newVersionCode.toString())
}

@Serializable
data class UploadedInfos(val commit:String?, val buildNumber:Int)

@Serializable
data class ParseUploadedInfosResponse(val result : UploadedInfos)
val json by lazy {
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}

private fun Project.skComputeVersionCodeAndReleaseNote(nbMaxCommitsInReleaseNote:Int) {
    task("compute_version_code_and_release_note") {
        doFirst {
            println("setting version Code from server")
            println("--fetch last uploaded versionCode")
            val lastUploadedInfos:UploadedInfos? =
                try {
                    val strInfos = commandLine("scripts/versions/getLastUploadedInfos.sh")
//                    val tab = strInfos.split("_")
//                    UploadedInfos(tab[0], tab[1].toInt())
                    if(strInfos.contains("result")){
                        //back4app version server
                        json.decodeFromString(ParseUploadedInfosResponse.serializer(), strInfos).result
                    }else {
                        //ua version server
                        json.decodeFromString(UploadedInfos.serializer(), strInfos)
                    }
                }
                catch (exception: Exception) {
                    null
                }
            println("--fetched last uploaded infos = $lastUploadedInfos")
            val currentVersionCode = skVersionCode()
            if (lastUploadedInfos != null) {
                skSetVersionCode(max(lastUploadedInfos.buildNumber, currentVersionCode + 1))
                val lastCommitHashes = commandLine("git", "show", "-s", "--format=%h", "-$nbMaxCommitsInReleaseNote").split("\n").filter { it.isNotBlank() }
                val lastUploadCommitIndex = lastCommitHashes.indexOf(lastUploadedInfos.commit)
                val nbCommitsInNote = if (lastUploadCommitIndex != -1) {
                    lastUploadCommitIndex
                } else {
                    Math.min(nbMaxCommitsInReleaseNote, 1)
                }
                val noteContent = commandLine("git", "show", "-s", "--format=%m%s%n%b%n", "-$nbCommitsInNote")
                rootDir.toPath().writeStringTo("androidApp/distribution/release-note.txt", noteContent, true)

            }
        }
        group = "skot_versions"
    }
}

private fun Project.skSaveUploadedInfos() {
    task("save_uploaded_versions_infos") {
        doFirst {
            val lastCommitHash = commandLine("git", "show", "-s", "--format=%h").substringBefore("\n")
            commandLine("scripts/versions/saveLastUploadedInfos.sh", lastCommitHash)
        }
        group = "skot_versions"
    }
}



@Deprecated(
    message = "The current branch is automatically selected no need of branchEnvVariable and defaultBranch anymore",
    replaceWith = ReplaceWith("skVersionsTasks(nbMaxCommitsInReleaseNote)"),
    level = DeprecationLevel.ERROR
)
fun Project.skVersionsTasks(branchEnvVariable:String, defaultBranch:String, nbMaxCommitsInReleaseNote:Int) {
    skVersionsTasks(nbMaxCommitsInReleaseNote)
}
fun Project.skVersionsTasks(nbMaxCommitsInReleaseNote:Int) {
    skComputeVersionCodeAndReleaseNote(nbMaxCommitsInReleaseNote)
    skSaveUploadedInfos()
}