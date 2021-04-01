package tech.skot.tools.starter

import tech.skot.starter.buildsrc.buildSrc
import tech.skot.tools.generation.writeLinesTo
import tech.skot.tools.generation.writeStringTo
import tech.skot.tools.starter.androidApp.androidApp
import tech.skot.tools.starter.model.model
import tech.skot.tools.starter.modelcontract.modelContract
import tech.skot.tools.starter.view.view
import tech.skot.tools.starter.viewcontract.viewContract
import tech.skot.tools.starter.viewmodel.viewModel
import java.nio.file.Path

class StarterGenerator(val rootDir: Path, val configuration: StarterConfiguration) {

    var modules = mutableListOf<String>()

    fun generateSkeletton() {

        rootDir.writeStringTo(".gitignore", gitIgnore)

        println("---generate buildSrc")
        buildSrc()
        println("---generate viewContract")
        viewContract()
        println("---generate modelContract")
        modelContract()
        println("---generate view")
        view()
        println("---generate model")
        model()
        println("---generate viewModel")
        viewModel()
        println("---generate android App")
        androidApp()

        println("---generate skot module")
        skotModule()


        println("---generate settings.gradle.kts")
        rootDir.writeLinesTo("settings.gradle.kts", modules.map {
            "include(\":$it\")"
        }, evenIfExists = true)

        println("---generate build.gradle.kts")
        rootDir.writeStringTo("build.gradle.kts", rootBuildGradle, evenIfExists = true)

        rootDir.writeStringTo("skot_librairies.properties", "//Add here dependencies to Skot Libraries\n", evenIfExists = false)

        rootDir.writeStringTo("gradle.properties", """android.enableJetifier=true
org.gradle.jvmargs=-XX\:MaxHeapSize\=4096 -Xmx4096M
org.gradle.daemon=true
android.useAndroidX=true""")
//        rootDir.writeLinesTo("settings.gradle.kts", modules.map { "include(\":$it\")" })
    }


}