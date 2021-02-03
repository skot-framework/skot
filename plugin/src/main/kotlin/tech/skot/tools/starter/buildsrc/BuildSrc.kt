package tech.skot.starter.buildsrc

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import tech.skot.Versions
import tech.skot.tools.generation.writeLinesTo
import tech.skot.tools.starter.StarterGenerator
import tech.skot.tools.starter.buildsrc.buildGradle

fun StarterGenerator.buildSrc() {
    rootDir.writeLinesTo("buildSrc/build.gradle.kts", buildGradle())

    FileSpec.builder("", "Build")
            .addType(
                    TypeSpec.objectBuilder("Build")
                            .addProperty(propertySpec = PropertySpec.builder("appName", String::class, KModifier.CONST).initializer("\"${configuration.appName}\"").build())
                            .addProperty(propertySpec = PropertySpec.builder("versionCode", Int::class, KModifier.CONST).initializer("1").build())
                            .addProperty(propertySpec = PropertySpec.builder("versionName", String::class, KModifier.CONST).initializer("\"0.0.0\"").build())
                            .build()
            ).build().writeTo(rootDir.resolve("buildSrc/src/main/kotlin"))

    FileSpec.builder("", "Versions")
            .addType(
                    TypeSpec.objectBuilder("Versions")
                            .addProperty(propertySpec = PropertySpec.builder("skot", String::class, KModifier.CONST).initializer("\"${Versions.skot}\"").build())
                            .build()
            ).build().writeTo(rootDir.resolve("buildSrc/src/main/kotlin"))

    configuration.appName
}