package tech.skot.generator

import org.w3c.dom.Element
import java.nio.file.Path
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.reflect.KClass
import kotlin.reflect.KType

fun getPackageName(path: Path): String {
    val manifest = path.resolve("AndroidManifest.xml")
    return manifest.getDocumentElement().getAttribute("package")
}

fun List<String>.packageToPath() = map { it.replace('.','/') }.joinToString(separator = "/")


fun Path.getDocumentElement(): Element =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.toFile()).documentElement

fun Element.childElements(): List<Element> {
    val elements: MutableList<Element> = mutableListOf()
    for (i in 0 until childNodes.length) {
        (childNodes.item(i) as? Element)?.let { elements.add(it) }
    }
    return elements
}

fun KClass<*>.`package`() = this.java.`package`
fun KClass<*>.packageName() = this.java.`package`.name
fun KType.isAny() = this.classifier == Any::class

val appPackageName by lazy {
    getPackageName(Paths.get("../app/src/main"))
}
