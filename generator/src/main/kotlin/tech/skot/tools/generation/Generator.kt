package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import org.w3c.dom.Element
import tech.skot.core.components.ScreenVC
import tech.skot.core.components.ComponentVC
import tech.skot.core.components.Opens
import tech.skot.tools.generation.viewlegacy.*
import tech.skot.tools.generation.viewmodel.generateViewModel
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object Modules {
    const val viewcontract = "viewcontract"
    const val view = "view"
    const val viewmodel = "viewmodel"
}

class Generator(
        val appPackage: String,
        private val startClass: KClass<ScreenVC>,
        val baseActivity: ClassName?,
        val rootPath: Path
) {


    val components = mutableSetOf<KClass<out ComponentVC>>().apply {
        addLinkedComponents(startClass, appPackage)
    }.map { it.def() }

    val mapTypeDef = components.map { it.vc.asTypeName() to it }.toMap()
    init {
        println("les components : $components")
    }

    val viewInjector = ClassName("$appPackage.di","ViewInjector")
    val viewInjectorIntance = ClassName("$appPackage.di","viewInjector")


    fun generate() {
//        println("screenDef : ${startClass.toComponentDef()}")

        generateViewContract()
        generateViewLegacy()
        generateViewModel()
    }

    fun generatedCommonSources(module: String) =
            rootPath.resolve("$module/generated/commonMain/kotlin")

    fun commonSources(module: String) =
            rootPath.resolve("$module/src/commonMain/kotlin")

    fun generatedAndroidSources(module: String) =
            rootPath.resolve("$module/generated/androidMain/kotlin")

    fun androidSources(module: String) =
            rootPath.resolve("$module/src/androidMain/kotlin")

    fun deleteModuleGenerated(module :String) {
        rootPath.resolve("$module/generated").toFile().deleteRecursively()
    }

    private fun generateViewContract() {
        deleteModuleGenerated(Modules.viewcontract)
        generateViewInjector()
    }

    private fun generateViewInjector() {
        FileSpec.builder(
                viewInjector.packageName,
                viewInjector.simpleName

        ).addType(TypeSpec.interfaceBuilder(viewInjector.simpleName)
                .addFunctions(
                        components.map {
                            FunSpec.builder(it.name.decapitalize())
                                    .addModifiers(KModifier.ABSTRACT)
                                    .addParameters(
                                            it.subComponents.map { it.asParam() }
                                    )
                                    .addParameters(
                                            it.fixProperties.map { it.asParam() }
                                    )
                                    .addParameters(
                                            it.mutableProperties.map { it.initial().asParam() }
                                    )
                                    .returns(it.vc)
                                    .build()
                        }
                )
                .build())
                .build().writeTo(generatedAndroidSources(Modules.viewcontract))
    }

    fun ClassName.existsAndroidInModule(module: String) =
        Files.exists(androidSources(module).resolve(packageName.packageToPathFragment()).resolve("$simpleName.kt"))

    fun ClassName.existsCommonInModule(module: String) =
            Files.exists(commonSources(module).resolve(packageName.packageToPathFragment()).resolve("$simpleName.kt"))

    fun androidResLayoutPath(module:String, name:String) =
            rootPath.resolve("$module/src/androidMain/res/layout/$name.xml")

}


fun getAndroidPackageName(path: Path): String {
    val manifest = path.resolve("AndroidManifest.xml")
    return manifest.getDocumentElement().getAttribute("package")
}

//fun List<String>.packageToPath() = map { it.replace('.','/') }.joinToString(separator = "/")


fun Path.getDocumentElement(): Element =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.toFile()).documentElement

fun Element.childElements(): List<Element> {
    val elements: MutableList<Element> = mutableListOf()
    for (i in 0 until childNodes.length) {
        (childNodes.item(i) as? Element)?.let { elements.add(it) }
    }
    return elements
}
