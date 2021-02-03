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
import java.nio.file.Files
import java.nio.file.Path

class StarterGenerator(val rootDir: Path, val configuration: StarterConfiguration) {

    var modules = mutableListOf<String>()

    fun generateSkeletton() {

        rootDir.writeStringTo(".gitignore", gitIgnore)

        buildSrc()
        viewContract()
        modelContract()
        view()
        model()
        viewModel()
        androidApp()

        rootDir.writeLinesTo("settings.gradle.kts", modules.map {
            "include(\":$it\")"
        }, evenIfExists = true)

        rootDir.writeStringTo("build.gradle.kts", rootBuildGradle, evenIfExists = true)

//        rootDir.writeLinesTo("settings.gradle.kts", modules.map { "include(\":$it\")" })
    }


}