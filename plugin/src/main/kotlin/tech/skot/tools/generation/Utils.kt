package tech.skot.tools.generation

import java.nio.file.Files
import java.nio.file.Path

fun block(command:String, content:List<String>):List<String> {
    val lines = mutableListOf<String>()
    lines.add("$command {")
    lines.addAll(content.map { "\t$it" })
    lines.add("}")
    return lines
}

fun Path.writeLinesTo(path:String, content:List<String>, evenIfExists:Boolean = false) {
   writeStringTo(path, content = content.joinToString("\n"), evenIfExists = evenIfExists)
}

fun Path.writeStringTo(path:String, content:String, evenIfExists:Boolean = false) {
    val target = resolve(path)
    if (!evenIfExists && Files.exists(target)) return

    if (!Files.exists(target)) {
        Files.createDirectories(target.parent)
    }
    target.toFile().writeText(content)
}


fun List<String>.tab():List<String> = map { "\t$it" }