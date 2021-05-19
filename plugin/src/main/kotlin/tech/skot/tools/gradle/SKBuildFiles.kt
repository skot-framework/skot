package tech.skot.tools.gradle

import com.squareup.kotlinpoet.asTypeName
import org.gradle.api.Project
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

//var skDebugMode:Boolean = false

fun copyBuildFileToImplementation(build: Any, project: Project, addingVersionCodeAndDebug:Boolean, debug:Boolean) {
    println("Copy of build object :${build::class.simpleName} addingVersionCodeAndDebug: $addingVersionCodeAndDebug  with debug: $debug")
    val stringType = String::class.createType()
    val intType = Int::class.createType()

    val buildObjectType = build::class.asTypeName()
    val file = com.squareup.kotlinpoet.FileSpec.builder(
        buildObjectType.packageName,
        buildObjectType.simpleName
    )
    val classBuilderCommon =
        com.squareup.kotlinpoet.TypeSpec.objectBuilder(buildObjectType.simpleName)
            .apply {
                if (addingVersionCodeAndDebug) {
                    println("----with debug $debug")
                    addProperty(
                        com.squareup.kotlinpoet.PropertySpec.builder(
                            "versionCode",
                            Int::class,
                            com.squareup.kotlinpoet.KModifier.CONST
                        )
                            .initializer(project.skVersionCode().toString())
                            .build()
                    )

                    addProperty(
                        com.squareup.kotlinpoet.PropertySpec.builder(
                            "debug",
                            Boolean::class,
                            com.squareup.kotlinpoet.KModifier.CONST
                        )
                            .initializer(debug.toString())
                            .build()
                    )

                }

                build::class.memberProperties.forEach {
                    when (it.returnType) {
                        stringType -> {
                            addProperty(
                                com.squareup.kotlinpoet.PropertySpec.builder(
                                    it.name,
                                    String::class,
                                    com.squareup.kotlinpoet.KModifier.CONST
                                )
                                    .initializer("\"${it.call()}\"")
                                    .build()
                            )
                        }
                        intType -> {
                            addProperty(
                                com.squareup.kotlinpoet.PropertySpec.builder(
                                    it.name,
                                    Int::class,
                                    com.squareup.kotlinpoet.KModifier.CONST
                                )
                                    .initializer(it.call().toString())
                                    .build()
                            )
                        }
                    }

                }
            }
    file.addType(classBuilderCommon.build())
    file.build().writeTo(project.projectDir.resolve("generated/commonMain/kotlin"))
}