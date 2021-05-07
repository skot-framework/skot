package tech.skot.tools.starter.buildsrc

import tech.skot.tools.starter.BuildGradleGenerator


fun buildGradle() = BuildGradleGenerator().apply {
    repository(BuildGradleGenerator.Repository.MavenCentral)
    plugin(BuildGradleGenerator.Plugin.KotlinDsl)
}.generate()
