package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.w3c.dom.Element
import tech.skot.core.components.ScreenVC
import tech.skot.core.components.ComponentVC
import tech.skot.tools.generation.viewlegacy.*
import tech.skot.tools.generation.viewmodel.generateViewModel
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.reflect.KClass

object Modules {
    const val app = "androidApp"
    const val viewcontract = "viewcontract"
    const val modelcontract = "modelcontract"
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

//    val mapTypeDef = components.map { it.vc.asTypeName() to it }.toMap()


    val viewInjectorInterface = ClassName("$appPackage.di","ViewInjector")
    val viewInjectorImpl = ClassName("$appPackage.di","ViewInjectorImpl")
    val viewInjectorIntance = ClassName("$appPackage.di","viewInjector")

    val stringsInstance = ClassName(appPackage, "strings")
    val stringsInterface = ClassName(appPackage, "Strings")
    val stringsImpl = ClassName(appPackage, "StringsImpl")

    val pluralsInstance = ClassName(appPackage, "plurals")
    val pluralsInterface = ClassName(appPackage, "Plurals")
    val pluralsImpl = ClassName(appPackage, "PluralsImpl")

    val iconsInstance = ClassName(appPackage, "icons")
    val iconsInterface = ClassName(appPackage, "Icons")
    val iconsImpl = ClassName(appPackage, "IconsImpl")


    val generatedAppModule = ClassName("$appPackage.di", "generatedAppModule")

    val moduleFun = ClassName("tech.skot.core.di", "module")
    val module = ClassName("tech.skot.core.di", "Module")
    val getFun = ClassName("tech.skot.core.di", "get")
    val baseInjector = ClassName("tech.skot.core.di", "BaseInjector")

    fun generate() {

        generateViewContract()
        generateViewLegacy()
        generateViewModel()
        deleteModuleGenerated(Modules.modelcontract)
        deleteModuleGenerated(Modules.app)
        generateStrings()
        generatePlurals()
        generateIcons()
        generateApp()
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
                viewInjectorInterface.packageName,
                viewInjectorInterface.simpleName

        ).addType(TypeSpec.interfaceBuilder(viewInjectorInterface.simpleName)
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
                .build().writeTo(generatedCommonSources(Modules.viewcontract))
    }

    fun ClassName.existsAndroidInModule(module: String) =
        Files.exists(androidSources(module).resolve(packageName.packageToPathFragment()).resolve("$simpleName.kt"))

    fun ClassName.existsCommonInModule(module: String) =
            Files.exists(commonSources(module).resolve(packageName.packageToPathFragment()).resolve("$simpleName.kt"))

    fun androidResLayoutPath(module:String, name:String) =
            rootPath.resolve("$module/src/androidMain/res/layout/$name.xml")

    val viewR = ClassName("$appPackage.${Modules.view}", "R")
    val appR = ClassName("$appPackage.android", "R")



    fun generateApp() {
        generateAppModule()
    }


    fun generateAppModule() {
        FileSpec.builder(generatedAppModule.packageName, generatedAppModule.simpleName)
                .addProperty(
                        PropertySpec.builder(generatedAppModule.simpleName,module.parameterizedBy(baseInjector))
                                .initializer(CodeBlock.builder()
                                        .beginControlFlow("module")
                                        .addStatement("single { ${stringsImpl.simpleName}(androidApplication) as ${stringsInterface.simpleName}}")
                                        .addStatement("single { ${pluralsImpl.simpleName}(androidApplication) as ${pluralsInterface.simpleName}}")
                                        .addStatement("single { ${iconsImpl.simpleName}() as ${iconsInterface.simpleName}}")
                                        .addStatement("single { ${viewInjectorImpl.simpleName}() as ${viewInjectorInterface.simpleName}}")
                                        .endControlFlow()
                                        .build())
                                .build()

                )
                .addImportClassName(getFun)
                .addImportClassName(moduleFun)
                .addImportClassName(baseInjector)
                .addImportClassName(stringsImpl)
                .addImportClassName(stringsInterface)
                .addImportClassName(pluralsImpl)
                .addImportClassName(pluralsInterface)
                .addImportClassName(viewInjectorImpl)
                .addImportClassName(viewInjectorInterface)
                .build()
                .writeTo(generatedAndroidSources(Modules.app))
    }

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
