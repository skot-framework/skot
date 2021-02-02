package tech.skot.tools.starter.buildsrc

import tech.skot.tools.starter.BuildGradleGenerator


fun buildGradle() = BuildGradleGenerator().apply {
    repository(BuildGradleGenerator.Repository.JCenter)
    plugin(BuildGradleGenerator.Plugin.KotlinDsl)
}.generate()
