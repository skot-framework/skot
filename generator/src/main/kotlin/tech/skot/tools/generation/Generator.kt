package tech.skot.tools.generation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.w3c.dom.Document
import org.w3c.dom.Element
import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKScreenVC
import tech.skot.tools.generation.model.generateModel
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
    const val model = "model"
}


class Generator(
        val appPackage: String,
        val startClass: KClass<SKScreenVC>,
        val baseActivity: ClassName?,
        val rootPath: Path
) {


    val components = mutableSetOf<KClass<out SKComponentVC>>().apply {
        addLinkedComponents(startClass, appPackage)
    }.map { it.def() }

    val componentsWithModel = components.filter {
        it.modelContract().existsCommonInModule(Modules.modelcontract)
    }



//    val mapTypeDef = components.map { it.vc.asTypeName() to it }.toMap()


    val viewInjectorInterface = ClassName("$appPackage.di", "ViewInjector")
    val viewInjectorImpl = ClassName("$appPackage.di", "ViewInjectorImpl")
    val viewInjectorIntance = ClassName("$appPackage.di", "viewInjector")

    val modelInjectorInterface = ClassName("$appPackage.di", "ModelInjector")
    val modelInjectorImpl = ClassName("$appPackage.di", "ModelInjectorImpl")
    val modelInjectorIntance = ClassName("$appPackage.di", "modelInjector")


    val stringsInstance = ClassName(appPackage, "strings")
    val stringsInterface = ClassName(appPackage, "Strings")
    val stringsImpl = ClassName(appPackage, "StringsImpl")

    val pluralsInstance = ClassName(appPackage, "plurals")
    val pluralsInterface = ClassName(appPackage, "Plurals")
    val pluralsImpl = ClassName(appPackage, "PluralsImpl")

    val iconsInstance = ClassName(appPackage, "icons")
    val iconsInterface = ClassName(appPackage, "Icons")
    val iconsImpl = ClassName(appPackage, "IconsImpl")

    val colorsInstance = ClassName(appPackage, "colors")
    val colorsInterface = ClassName(appPackage, "Colors")
    val colorsImpl = ClassName(appPackage, "ColorsImpl")


    val generatedAppModules = ClassName("$appPackage.di", "generatedAppModules")

    val moduleFun = ClassName("tech.skot.core.di", "module")
    val module = ClassName("tech.skot.core.di", "Module")
    val getFun = ClassName("tech.skot.core.di", "get")
    val baseInjector = ClassName("tech.skot.core.di", "BaseInjector")

    fun generate() {

        generateViewContract()
        generateViewLegacy()
        generateViewModel()
        generateModelContract()
        generateModel()
        deleteModuleGenerated(Modules.app)
        generateStrings()
        generatePlurals()
        generateIcons()
        generateColors()
        generateApp()
        generateCodeMap()
    }

    fun generatedCommonSources(module: String) =
            rootPath.resolve("$module/generated/commonMain/kotlin")

    fun commonSources(module: String) =
            rootPath.resolve("$module/src/commonMain/kotlin")

    fun generatedAndroidSources(module: String) =
            rootPath.resolve("$module/generated/androidMain/kotlin")

    fun androidSources(module: String) =
            rootPath.resolve("$module/src/androidMain/kotlin")

    fun deleteModuleGenerated(module: String) {
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
                                            it.mutableProperties.map { it.initial().asParam(withDefaultNullIfNullable = true) }
                                    )
                                    .returns(it.vc)
                                    .build()
                        }
                )
                .build())
                .build().writeTo(generatedCommonSources(Modules.viewcontract))
    }

    private fun generateModelContract() {
        deleteModuleGenerated(Modules.modelcontract)
        generateModelInjector()
    }

    private fun generateModelInjector() {
        modelInjectorInterface.fileInterfaceBuilder {
            addFunctions(
                    componentsWithModel.map {
                        FunSpec.builder(it.name.decapitalize())
                                .addModifiers(KModifier.ABSTRACT)
                                .returns(it.modelContract())
                                .build()
                    }
            )
        }.writeTo(generatedCommonSources(Modules.modelcontract))
    }

    fun ClassName.existsAndroidInModule(module: String) =
            Files.exists(androidSources(module).resolve(packageName.packageToPathFragment()).resolve("$simpleName.kt"))

    fun ClassName.existsCommonInModule(module: String) =
            Files.exists(commonSources(module).resolve(packageName.packageToPathFragment()).resolve("$simpleName.kt"))

    fun androidResLayoutPath(module: String, name: String) =
            rootPath.resolve("$module/src/androidMain/res/layout/$name.xml")

    val viewR = ClassName("$appPackage.${Modules.view}", "R")
    val appR = ClassName("$appPackage.android", "R")


    fun generateApp() {
        generateAppModule()
    }


    fun generateAppModule() {
        val librariesGroups = getUsedSKLibrariesGroups()

        FileSpec.builder(generatedAppModules.packageName, generatedAppModules.simpleName)
                .addProperty(
                        PropertySpec.builder(generatedAppModules.simpleName, ClassName("kotlin.collections", "List").parameterizedBy(module.parameterizedBy(baseInjector)))
                                .initializer(CodeBlock.builder()
                                        .beginControlFlow("listOf(module")
                                        .addStatement("single { ${stringsImpl.simpleName}(androidApplication) as ${stringsInterface.simpleName}}")
                                        .addStatement("single { ${pluralsImpl.simpleName}(androidApplication) as ${pluralsInterface.simpleName}}")
                                        .addStatement("single { ${iconsImpl.simpleName}() as ${iconsInterface.simpleName}}")
                                        .addStatement("single { ${colorsImpl.simpleName}() as ${colorsInterface.simpleName}}")
                                        .addStatement("single { ${viewInjectorImpl.simpleName}() as ${viewInjectorInterface.simpleName}}")
                                        .addStatement("single { ${modelInjectorImpl.simpleName}() as ${modelInjectorInterface.simpleName}}")
                                        .endControlFlow()
                                        .addStatement(",")
                                        .addStatement("modelFrameworkModule,")
                                        .addStatement("coreViewModule,")
                                        .apply {
                                            librariesGroups
                                                    .map {
                                                        "${it.substringAfterLast(".")}Module"
                                                    }.forEach {
                                                        addStatement(it)
                                                    }

                                        }
                                        .addStatement(")")
                                        .build())
                                .build()

                )
//                .addImportClassName(getFun)
                .addImportClassName(moduleFun)
                .addImportClassName(baseInjector)
                .addImportClassName(stringsImpl)
                .addImportClassName(stringsInterface)
                .addImportClassName(pluralsImpl)
                .addImportClassName(pluralsInterface)
                .addImportClassName(iconsImpl)
                .addImportClassName(iconsInterface)
                .addImportClassName(colorsInterface)
                .addImportClassName(colorsImpl)
                .addImport("tech.skot.di", "modelFrameworkModule")
                .addImport("tech.skot.core.di", "coreViewModule")

                .apply {
                    getUsedSKLibrariesGroups().map {
                        addImport("$it.di", "${it.substringAfterLast(".")}Module")
                    }
                }
                .build()
                .writeTo(generatedAndroidSources(Modules.app))
    }


    fun getUsedSKLibrariesGroups(): List<String> {
        return Files.readAllLines(rootPath.resolve("skot_librairies.properties"))
                .filterNot { it.startsWith("//") }
                .map { it.substringBeforeLast(":") }
    }

    fun ComponentDef.hasModel() = componentsWithModel.contains(this)

}


fun getAndroidPackageName(path: Path): String {
    val manifest = path.resolve("AndroidManifest.xml")
    return manifest.getDocumentElement().getAttribute("package")
}

//fun List<String>.packageToPath() = map { it.replace('.','/') }.joinToString(separator = "/")


fun Path.getDocument(): Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.toFile())

fun Path.getDocumentElement(): Element =
        getDocument().documentElement

fun Element.childElements(): List<Element> {
    val elements: MutableList<Element> = mutableListOf()
    for (i in 0 until childNodes.length) {
        (childNodes.item(i) as? Element)?.let { elements.add(it) }
    }
    return elements
}
