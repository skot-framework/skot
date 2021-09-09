package tech.skot.tools.generation

import java.nio.file.Files
import kotlin.io.path.writeText

@ExperimentalStdlibApi
fun main(args: Array<String>) {
    buildGenerator(args).migrate()
}


fun Generator.migrateTo29() {

    val regexCallViewInjector = Regex("viewInjector.\\w*\\(")

    components.filter { it.isScreen }.forEach { component ->
        val file = component.viewModel().run {
            commonSources(modules.viewmodel).resolve(component.packageName.packageToPathFragment())
                .resolve("$simpleName.kt")
        }
        val text = String(Files.readAllBytes(file))
        val newText = text.replace(regexCallViewInjector) {
            it.value + "this, "
        }
        file.writeText(newText)

    }
}