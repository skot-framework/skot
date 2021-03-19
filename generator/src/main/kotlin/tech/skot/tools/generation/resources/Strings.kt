package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import java.nio.file.Files
import java.util.stream.Collectors


fun Generator.generateStrings() {

    val values = rootPath.resolve(Modules.view).resolve("src/androidMain/res_referenced/values")



    println("strings .........")
    val strings =
            if (!Files.exists(values)) {
                emptyList()
            } else {
                Files.list(values).flatMap {
                    it.getDocumentElement().childElements().stream().filter { it.tagName == "string" }
                            .map { it.getAttribute("name") }
                }.collect(Collectors.toList())
            }


    fun String.toStringsPropertyName() = decapitalize()


    FileSpec.builder(
            stringsInterface.packageName,
            stringsInterface.simpleName
    ).addType(TypeSpec.interfaceBuilder(stringsInterface.simpleName)
            .addProperties(
                    strings.map {
                        PropertySpec.builder(it.toStringsPropertyName(), String::class)
                                .build()
                    }
            )
            .build())
            .build()
            .writeTo(generatedCommonSources(Modules.modelcontract))


    stringsImpl.fileClassBuilder(listOf(viewR)) {
        addSuperinterface(stringsInterface)
        addPrimaryConstructorWithParams(listOf(ParamInfos("applicationContext", AndroidClassNames.context, listOf(KModifier.PRIVATE))))
        addFunction(
                FunSpec.builder("get")
                        .addModifiers(KModifier.PRIVATE)
                        .addParameter("strId", Int::class)
                        .returns(String::class)
                        .addStatement("return applicationContext.getString(strId)")
                        .build()
        )
        addProperties(
                strings.map {
                    PropertySpec.builder(it.toStringsPropertyName(), String::class, KModifier.OVERRIDE)
                            .getter(FunSpec.getterBuilder().addStatement("return get(R.string.$it)").build())
                            .build()
                }
        )
    }
            .writeTo(generatedAndroidSources(Modules.app))


}