package tech.skot.generator.viewcontract

import com.squareup.kotlinpoet.*
import tech.skot.contract.components.ComponentView
import tech.skot.generator.*
import java.io.File
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun ViewNode.buildConfsFilesAndInjector(moduleName: String) {
    val viewInjectorInterface = TypeSpec.interfaceBuilder("ViewInjector")

    val srcFile = Paths.get("../$moduleName/generated/commonMain/kotlin").toFile()
    buildConfsFiles(srcFile, mutableSetOf(), viewInjectorInterface)

    FileSpec.builder(appPackageName, "ViewInjector")
            .apply {
                addType(viewInjectorInterface.build())
            }.build().writeTo(srcFile)
}

fun ViewNode.buildConfsFiles(srcFile: File, alreadyDone: MutableSet<KClass<out ComponentView>>, viewInjectorInterface: TypeSpec.Builder) {
    viewClass.buildConfs(srcFile, alreadyDone, viewInjectorInterface)
    children?.forEach {
        it.buildConfsFiles(srcFile, alreadyDone, viewInjectorInterface)
    }
}

fun KClass<out ComponentView>.buildConfs(srcFile: File, alreadyDone: MutableSet<KClass<out ComponentView>>, viewInjectorInterface: TypeSpec.Builder) {
    val packageName = packageName()
    val confClassName = "${compName()}Conf"
    val confSpec = buildConf(confClassName)
    confSpec?.let {
        FileSpec.builder(packageName, confClassName)
                .apply {
                    addType(it)
                }.build().writeTo(srcFile)
    }
    val funBuilder = FunSpec.builder("${simpleName!!.substringBefore("View").decapitalize()}")
    funBuilder.returns(this)
    funBuilder.addModifiers(KModifier.ABSTRACT)
    confSpec?.let {
        funBuilder.addParameter(ParameterSpec("conf", ClassName(packageName, confClassName)))
    }
    ownMembers()
            .filter { it is KProperty }
            .filter {
                it.returnType.isComponentView() || it.returnType.isCollectionOfComponentView()
            }
            .forEach {
                funBuilder.addParameter(ParameterSpec(it.name, it.returnType.asTypeName()))
            }


    alreadyDone.add(this)
    subComponents()
            .fromApp()
            .forEach {
                if (!alreadyDone.contains(it)) {
                    it.buildConfs(srcFile, alreadyDone, viewInjectorInterface)
                }
            }
    viewInjectorInterface.addFunction(funBuilder.build())

}

fun KClass<out ComponentView>.buildConf(confClassName: String): TypeSpec? {

    val properties =
            ownMembers()
                    .filter { it is KProperty }
                    .filter {
                        !it.returnType.isComponentView() && !it.returnType.isCollectionOfComponentView()
                    }

    return if (properties.isNotEmpty()) {

        TypeSpec.classBuilder(confClassName)
                .primaryConstructor(
                        FunSpec.constructorBuilder()
                                .addParameters(
                                        properties.map {
                                            ParameterSpec(it.name, it.returnType.asTypeName())
                                        }
                                ).build()
                )
                .addProperties(
                        properties
                                .map {
                                    PropertySpec.builder(it.name, it.returnType.asTypeName())
                                            .initializer(it.name)
                                            .build()
                                }
                ).build()

    } else null

}



