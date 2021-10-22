package tech.skot.tools.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.*
import tech.skot.Versions
import java.nio.file.Files
import java.nio.file.Files.delete

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
    val initializationPlans:List<String> = emptyList(),
    val iOs:Boolean = false,
    val referenceIconsByVariant: Boolean = false
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


        val trueProjectDir = project.parent?.projectDir ?: project.rootDir
        val clearGenerated = project.task("skClearGenerated", type = Delete::class) {
            doLast {
                println("####### clearGenerated  $trueProjectDir ")
                println("####### clearGenerated  feature ${extension.app?.feature} ")
                extension.app?.feature?.let {
                    println("--------------- one feature to delete: $trueProjectDir/$it/generated")
                    trueProjectDir.toPath().resolve("$it/generated").toFile().deleteRecursively()
                }

            }
            group = "Skot"
            delete = setOf(
                "$trueProjectDir/viewcontract/generated",
                "$trueProjectDir/modelcontract/generated",
                "$trueProjectDir/androidApp/generated",
                "$trueProjectDir/model/generated",
                "$trueProjectDir/view/generated",
                "$trueProjectDir/viewmodel/generated",
            )
        }

//        project.task("skMigrateToAlpha30") {
//            doLast {
//                println("Skot version ${Versions.skot}")
//                val app = extension.app
//                if (app == null) {
//                    println("renseignez la configuration skot dans le build.gradle.kts du module skot .........")
//                } else {
//                    println("migrate .........")
//                    project.javaexec {
//                        main = "tech.skot.tools.generation.MigrateKt"
//                        classpath = sourceSet.runtimeClasspath
//                        args = listOf(
//                            app.packageName,
//                            app.startScreen,
//                            app.rootState.toString(),
//                            app.baseActivity ?: "null",
//                            (project.parent?.projectDir ?: project.rootDir).toPath().toString(),
//                            app.feature ?: "null",
//                            app.baseActivityVar ?: "null",
//                            app.initializationPlans.joinToString("_"),
//                        )
//                    }
//                }
//            }
//            dependsOn(project.tasks.getByName("compileKotlin"))
//            group = "Skot"
//
//        }

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
                            app.initializationPlans.joinToString("_"),
                            app.iOs.toString(),
                            app.referenceIconsByVariant.toString()
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

        }.dependsOn(clearGenerated)


    }


    private fun DependencyHandlerScope.dependencies(project: Project) {
        val parentProjectPath = project.parent?.path ?: ""

        this.add("implementation", project("$parentProjectPath:viewcontract"))
        this.add("implementation", project("$parentProjectPath:modelcontract"))
        this.add("api", "${Versions.group}:generator:${Versions.skot}")
        this.add("implementation", "com.pinterest:ktlint:0.42.1")
    }

}



