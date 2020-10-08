package tech.skot.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.components.ComponentView
import tech.skot.components.ScreenView
import tech.skot.contract.Model
import tech.skot.contract.SuperViewModelWithParams
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.superclasses

fun generateViewModel(
        moduleName: String,
        vararg mapsCustoms:Map<KClass<out ComponentView>, Pair<FileSpec.Builder.()->Unit,TypeSpec.Builder.()->Unit>>
) {
    val generatedDir = Paths.get("../$moduleName/generated/commonMain/kotlin")
    val srcDir = Paths.get("../$moduleName/src/commonMain/kotlin")

    println("generateViewModel----- $appPackageName")
    println("allComponents----- $allComponents")
    allComponents
            .fromApp()
            .filter { it.annotations.find { it is SuperViewModelWithParams } == null }
            .forEach {klass ->

                klass.buildCompGenFile(
                        customFile = mapsCustoms.mapNotNull {
                            it.get(klass)?.first
                        },
                        customType = mapsCustoms.mapNotNull {
                            it.get(klass)?.second
                        }
                ).writeTo(generatedDir)

                val compName = klass.compName()
                val packageName = klass.packageName()

                if (!srcDir.existsClass(packageName, compName)) {
                    klass.buildCompSkeletonFile().writeTo(srcDir)
                }
            }

}


fun KClass<out ComponentView>.buildCompGenFile(customFile:List<(FileSpec.Builder.()->Unit)>, customType:List<(TypeSpec.Builder.()->Unit)>): FileSpec {
    val componentGenName = compGenName()
    val packageName = packageName()

    return FileSpec.builder(packageName, componentGenName)
            .apply {
                if (isActualComponent() && appPackageName != packageName) {
                    addImport(appPackageName, "modelInjector")
                }
                customFile.forEach {
                    it.invoke(this)
                }
            }
            .addType(
                    TypeSpec.classBuilder(componentGenName)
                            .addModifiers(KModifier.ABSTRACT)
                            .apply {
                                if (!this@buildCompGenFile.isActualComponent()) {
                                    addTypeVariable(TypeVariableName("V", this@buildCompGenFile))
                                    superclass(superComponentClassName().parameterizedBy(TypeVariableName("V")))
                                } else {
                                    superclass(superComponentClassName().parameterizedBy(this@buildCompGenFile.asTypeName()))
                                }

                                annotations.find { it is Model }?.let {
                                    addProperty(
                                            PropertySpec.builder("model", (it as Model).modelInterface.asTypeName())
                                                    .initializer("modelInjector.${(it as Model).modelInterface.simpleName!!.decapitalize()}()")
                                                    .build())
                                }
                            }
                            .addProperties(
                                    subComponentMembers()
                                            .map {
                                                PropertySpec.builder(it.name, it.returnType.componentClassName(true)!!)
                                                        .addModifiers(KModifier.ABSTRACT)
                                                        .build()
                                            }
                            )
                            .apply {
                                if (subComponentMembers().isNotEmpty()) {
                                    addFunction(
                                            FunSpec.builder("onRemove")
                                                    .addModifiers(KModifier.OVERRIDE)
                                                    .addCode(
                                                            CodeBlock.builder()
                                                                    .apply {
                                                                        subComponentMembers()
                                                                                .forEach {
                                                                                    if (it.returnType.isCollectionOfComponentView()) {
                                                                                        add("${it.name}.forEach { it.onRemove()}\n")
                                                                                    } else {
                                                                                        add("${it.name}.onRemove()\n")
                                                                                    }

                                                                                }
                                                                    }
                                                                    .build())
                                                    .addCode("super.onRemove()")
                                                    .build())
                                }

                                customType.forEach { it.invoke(this) }
                            }
                            .build()
            )
            .build()
}

fun KClass<out ComponentView>.compGenName() = "${compName()}Gen"

fun KClass<out ComponentView>.buildCompSkeletonFile(): FileSpec {
    val compName = compName()
    val packageName = packageName()
    return FileSpec.builder(packageName, compName)
            .addType(
                    TypeSpec.classBuilder(compName)
                            .apply {
                                if (!isActualComponent()) {
                                    addTypeVariables(listOf(TypeVariableName("V", this@buildCompSkeletonFile.asTypeName())))
                                    addModifiers(KModifier.ABSTRACT)
                                }
                            }
                            .superclass(ClassName(packageName, compGenName())
                                    .let {
                                        if (!isActualComponent()) {
                                            it.parameterizedBy(TypeVariableName("V"))
                                        } else {
                                            it
                                        }
                                    })
                            .addProperties(
                                    subComponentMembers().map {
                                        PropertySpec.builder(it.name, it.returnType.componentClassName()!!)
                                                .addModifiers(KModifier.OVERRIDE)
                                                .initializer(toDoToGenerate)
                                                .build()

                                    }
                            )
                            .apply {
                                if (isActualComponent()) {
                                    addProperty(
                                            PropertySpec.builder("view", this@buildCompSkeletonFile)
                                                    .addModifiers(KModifier.OVERRIDE)
                                                    .initializer("viewInjector.${compName.decapitalize()}(${subComponentMembers().map { it.componentToView() }.joinToString(", ")})")
                                                    .build()
                                    )
                                }
                            }

                            .build()
            )
            .apply {
                if (isActualComponent() && appPackageName != packageName) {
                    addImport(appPackageName, "viewInjector")
                }
            }
            .build()
}

