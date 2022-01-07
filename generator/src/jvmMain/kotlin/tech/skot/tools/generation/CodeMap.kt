package tech.skot.tools.generation

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

@ExperimentalStdlibApi
fun Generator.generateCodeMap() {

    fun buildMap(): String {
        val builder = StringBuilder()
        //on groupe les composants par package:
        components
                .groupBy { it.packageName }
                .forEach { (pack, comps) ->
                    val tab = pack.split(".").map { '_' }.joinToString("")
                    val biggestNameLength = comps.map { it.name.length }.maxOrNull() ?: 20
                    builder.appendLine(pack)
                    comps
                        .sortedBy { it.name }.forEach { comp ->
                        builder.appendLine("")
                        val endTab = (comp.name.length..biggestNameLength+3).map { '_' }.joinToString("")
                        builder.appendLine("$tab ${comp.name} $endTab")
//                            builder.appendLine("[Binding][$appPackage.view.databinding.${comp.name}Binding]")
                        builder.appendLine("[View][${comp.viewImpl()}]")
                        builder.appendLine("[VC][${comp.viewContract()}]")
                        builder.appendLine("[VM][${comp.viewModel()}]")
                        if (comp.hasModel()) {
                            builder.appendLine("[MC][${comp.modelContract()}]")
                            builder.appendLine("[Model][${comp.model()}]")
                        }



                    }
                    builder.appendLine("")

                }

        if (bmsMap.isNotEmpty()) {
            builder.appendLine("")
            builder.appendLine("")
            builder.appendLine("Les BusinessModel")
            bmsMap.keys.forEach {
                builder.appendLine("")
                builder.appendLine("[${it.simpleName}][$it]")
            }
        }

        return builder.toString()
    }

    FileSpec.builder(packageName = appPackage, "CodeMap")
            .addType(
                    TypeSpec.classBuilder("CodeMap")
                            .addKdoc(buildMap())
                            .build()
            )
            .build()
            .writeTo(commonSources(feature ?: modules.app))

}



