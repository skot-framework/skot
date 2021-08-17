package tech.skot.tools.gradle

import org.gradle.api.Project
import java.io.File

//fun getProjectPath(): String = File("..").absolutePath
//
//fun projectDir(): File = File(getProjectPath())

fun Project.commandLine(vararg strCmd: String): String {
    val process = ProcessBuilder(strCmd.asList())
        .directory(projectDir)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
    val res = String(process.inputStream.readBytes())
    process.waitFor()
    process.destroy()
    return res
}
