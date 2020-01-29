package tech.skot.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import tech.skot.components.ComponentView
import java.nio.file.Paths
import kotlin.reflect.KClass

fun generateViewModel(moduleName: String) {
    val generatedDir = Paths.get("../$moduleName/generated/commonMain/kotlin")
    val srcDir = Paths.get("../$moduleName/src/commonMain/kotlin")

    allComponents
            .fromApp()
            .forEach {
                it.buildCompGenFile().writeTo(generatedDir)


                val compName = it.compName()
                val packageName = it.packageName()

                if (!srcDir.existsClass(packageName, compName)) {
                    it.buildCompSkeletonFile().writeTo(srcDir)
                }
            }

}


fun KClass<out ComponentView>.buildCompGenFile(): FileSpec {
    val componentGenName = compGenName()
    val packageName = packageName()

    return FileSpec.builder(packageName, componentGenName)
            .addType(
                    TypeSpec.classBuilder(componentGenName)
                            .addModifiers(KModifier.ABSTRACT)
                            .apply {
                                if (!this@buildCompGenFile.isActualComponent()) {
                                    addTypeVariable(TypeVariableName("V", this@buildCompGenFile))
                                    superclass(superComponentClassName().parameterizedBy(TypeVariableName("V")))
                                }
                                else {
                                    superclass(superComponentClassName().parameterizedBy(asTypeName()))
                                }
                            }
                            .addProperties(
                                    subComponentMembers()
                                            .map {
                                                PropertySpec.builder(it.name, it.returnType.componentClassName()!!)
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
                            }
                            .build()
            )
            .build()
}

fun KClass<out ComponentView>.compGenName() = "${simpleName!!.substringBefore("View")}Gen"

fun KClass<out ComponentView>.buildCompSkeletonFile(): FileSpec {
    val compName = compName()
    val packageName = packageName()
    return FileSpec.builder(packageName, compName)
            .addType(
                    TypeSpec.classBuilder(compName)
                            .superclass(ClassName(packageName, compGenName()))
                            .addProperties(
                                    subComponentMembers().map {
                                        PropertySpec.builder(it.name, it.returnType.asTypeName())
                                                .addModifiers(KModifier.OVERRIDE)
                                                .initializer(toDoToGenerate)
                                                .build()

                                    }
                            )
                            .addProperty(
                                    PropertySpec.builder("view", this)
                                            .addModifiers(KModifier.OVERRIDE)
                                            .initializer("viewInjector.${compName.decapitalize()}(${subComponentMembers().map { it.componentToView() }.joinToString(", ")})")
                                            .build()
                            )
                            .build()
            )
            .build()
}

