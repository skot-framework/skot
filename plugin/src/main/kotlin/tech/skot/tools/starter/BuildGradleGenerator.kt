package tech.skot.tools.starter

import tech.skot.tools.generation.block

class BuildGradleGenerator {

    val publish = mutableListOf<String>()
    fun publish(group:String, version:String) {
        publish.add("group = $group")
        publish.add("version = $version")
        publish.add("")
    }


    sealed class Repository(val name:String) {
        open fun generate() = listOf("$name()")

        object Google:Repository("google")
        object JCenter:Repository("jcenter")
        object MavenLocal:Repository("mavenLocal")
        class Maven(val uri:String):Repository("maven") {
            override fun generate()  = block("maven", listOf("url = uri(\"$uri\")"))
        }
    }

    fun List<Repository>.repositories():List<String> = block("repositories", flatMap { it.generate() })


    sealed class Plugin() {
        abstract fun generate():String
        class Kotlin(val module:String):Plugin() {
            override fun generate() = "kotlin(\"$module\")"
        }
        object KotlinDsl:Plugin() {
            override fun generate() = "`kotlin-dsl`"
        }
        class Id(val id:String, val version:String? = null):Plugin() {
            override fun generate() = "id(\"$id\")${version?.let { ".version(\"$it\")" } ?: ""}"
        }
    }

    fun List<Plugin>.plugins():List<String> = block("plugins", map { it.generate() })


    private val rootPlugins = mutableListOf<Plugin>()

    fun plugin(plugin:Plugin) {
        rootPlugins.add(plugin)
    }

    private val rootRepositories = mutableListOf<Repository>()
    fun repository(repository: Repository) {
        rootRepositories.add(repository)
    }

    var androidBlock:String? = null

    var manual:String? = null

    fun generate():List<String> {
        val lines = mutableListOf<String>()

        lines += publish

        if (rootPlugins.isNotEmpty()) {
            lines += rootPlugins.plugins() + ""
        }

        if (rootRepositories.isNotEmpty()) {
            lines += rootRepositories.repositories() + ""
        }

        androidBlock?.let {
            lines += it.split("\n")
        }

        manual?.let {
            lines += it.split("\n")
        }


        return lines
    }

}


