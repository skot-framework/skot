package tech.skot.generator

import com.squareup.kotlinpoet.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun buildStringsFile(moduleName:String) {
    val module = Paths.get("../$moduleName")
    val values = module.resolve("src/androidMain/res/values")
    val modulePackageName = getPackageName(Paths.get("../$moduleName/src/androidMain"))


    val strings =
            Files.list(values).filter { it.fileName.toString().startsWith("strings") }.flatMap {
                it.getDocumentElement().childElements().stream().filter { it.tagName == "string" }
                        .map { it.getAttribute("name") }
            }.collect(Collectors.toList())


    //Construction du fichier
    val contextClassName = ClassName("android.content", "Context")


    val fileAndroid = FileSpec.builder("$appPackageName.model", "Strings")
    val classBuilderAndroid = TypeSpec.classBuilder("StringsGen")
            .addSuperinterface(ClassName("$appPackageName.model", "Strings"))
            .primaryConstructor(
                    FunSpec.constructorBuilder()
                            .addParameter("applicationContext", contextClassName)
                            .build()
            )
            .addProperty(
                    PropertySpec.builder(
                            "applicationContext",
                            contextClassName
                    ).initializer("applicationContext").build()
            )
            .addFunction(
                    FunSpec.builder("get")
                            .addParameter("strInt",Int::class)
                            .returns(String::class)
                            .addCode(CodeBlock.of("return applicationContext.getString(strInt)"))
                            .build()
            )
            .addProperties(
                    strings.map {
                        PropertySpec.builder(it, String::class, KModifier.OVERRIDE)
                                .initializer(CodeBlock.of("get(R.string.$it)"))
                                .build()
                    }
            )


    fileAndroid.addImport(modulePackageName, "R")
    fileAndroid.addType(classBuilderAndroid.build())

    fileAndroid.build().writeTo(module.resolve("generated/androidMain/kotlin").toFile())


    val fileCommon = FileSpec.builder("$appPackageName.model", "Strings")
    val classBuilderCommon = TypeSpec.interfaceBuilder("Strings")
            .addProperties(
                    strings.map {
                        PropertySpec.builder(it, String::class)
                                .build()
                    }
            )


    fileCommon.addType(classBuilderCommon.build())
    fileCommon.build().writeTo(module.resolve("generated/commonMain/kotlin").toFile())

}
