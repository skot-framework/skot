package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.*
import tech.skot.Versions

open class SKPluginToolsExtension {
    //    var startScreen: String? = null
//    var appPackage: String? = null
//    var baseActivity:String? = null
//    var featureModules:List<FeatureModule> = emptyList()
    var app: App? = null
}

data class App(
    val packageName: String,
    val startScreen: String,
    val rootState: String? = null,
    val baseActivity: String = ".android.BaseActivity",
    val feature: String? = null,
    //le fullname d'une variable globale donnant la classe de base
    val baseActivityVar: String? = null,
    val initializationPlans:List<String> = emptyList()
)

data class FeatureModule(val packageName: String, val startScreen: String)

class PluginTools : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<SKPluginToolsExtension>("skot")
        project.plugins.apply("java-library")
        project.plugins.apply("kotlin")

        project.dependencies {
            dependencies(project)
        }

        val javaPluginConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        val sourceSet = javaPluginConvention.sourceSets["main"]

        val cleanGeneratedContracts = project.task("skCleanGeneratedContracts") {
            doFirst {
                println("------- clean generated code in contracts modules")
                val rootPath = project.rootDir.toPath()
                rootPath.resolve("viewcontract/generated").toFile().deleteRecursively()
                rootPath.resolve("modelcontract/generated").toFile().deleteRecursively()
            }
            group = "Skot"
        }

        project.task("skMigrateToAlpha30") {
            doLast {
                println("Skot version ${Versions.skot}")
                val app = extension.app
                if (app == null) {
                    println("renseignez la configuration skot dans le build.gradle.kts du module skot .........")
                } else {
                    println("migrate .........")
                    project.javaexec {
                        main = "tech.skot.tools.generation.MigrateKt"
                        classpath = sourceSet.runtimeClasspath
                        args = listOf(
                            app.packageName,
                            app.startScreen,
                            app.rootState.toString(),
                            app.baseActivity ?: "null",
                            (project.parent?.projectDir ?: project.rootDir).toPath().toString(),
                            app.feature ?: "null",
                            app.baseActivityVar ?: "null",
                            app.initializationPlans.joinToString("_")
                        )
                    }
                }
            }
            dependsOn(project.tasks.getByName("compileKotlin"))
            group = "Skot"

        }

        project.task("skGenerate") {
            doLast {
                println("Skot version ${Versions.skot}")

                val app = extension.app
                if (app == null) {
                    println("rien à générer .........")
                } else {
                    println("génération .........")
                    project.javaexec {
                        main = "tech.skot.tools.generation.GenerateKt"
                        classpath = sourceSet.runtimeClasspath
                        args = listOf(
                            app.packageName,
                            app.startScreen,
                            app.rootState.toString(),
                            app.baseActivity ?: "null",
                            (project.parent?.projectDir ?: project.rootDir).toPath().toString(),
                            app.feature ?: "null",
                            app.baseActivityVar ?: "null",
                            app.initializationPlans.joinToString("_")
                        )
                    }
                }

                println("ktLint ......")
                val srcs = "**/generated/**/*.kt"
                project.javaexec {
                    workingDir = project.rootDir
                    main = "com.pinterest.ktlint.Main"
                    classpath = sourceSet.runtimeClasspath
                    args = listOf("-F", srcs)
                }
            }
            dependsOn(project.tasks.getByName("compileKotlin"))
            group = "Skot"

        }


    }


    private fun DependencyHandlerScope.dependencies(project: Project) {
        val parentProjectPath = project.parent?.path ?: ""

        this.add("implementation", project("$parentProjectPath:viewcontract"))
        this.add("implementation", project("$parentProjectPath:modelcontract"))
        this.add("api", "${Versions.group}:generator:${Versions.skot}")
        this.add("implementation", "com.pinterest:ktlint:0.42.1")
    }

}



