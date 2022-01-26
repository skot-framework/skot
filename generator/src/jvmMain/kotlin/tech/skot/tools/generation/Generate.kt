package tech.skot.tools.generation

import tech.skot.core.components.SKScreenVC
import tech.skot.tools.generation.viewmodel.InitializationPlan
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@ExperimentalStdlibApi
fun buildGenerator(args: Array<String>):Generator {
    val appPackage = args[0]

    val startClassFullName = args[1]?.let {
        if (it.startsWith(".")) {
            "$appPackage$it"
        }
        else {
            it
        }
    }
    val startClass = Class.forName(startClassFullName).kotlin
//    startClass.createInstance().


//    val compTester = Class.forName("com.sezane.tracking.TrackerFB").kotlin.createInstance() as ComponentInitializer
//    println(compTester.test())

    val rootStateClassFullName = args[2].let {
        if (it != "null") {
            if (it.startsWith(".")) {
                "$appPackage$it"
            }
            else {
                it
            }
        }
        else {
            null
        }

    }
    val rootStateClass = rootStateClassFullName?.let { Class.forName(it).kotlin }



    val strBaseActivity = args[3]

    println("########   $appPackage$strBaseActivity")
    val baseActivity =
        when {
            strBaseActivity.startsWith(".") -> {
                "$appPackage$strBaseActivity".fullNameAsClassName()
            }
            else -> {
                strBaseActivity.fullNameAsClassName()
            }
        }

    println("########   ${baseActivity.simpleName} ${baseActivity.packageName}")

    if (!startClass.isScreenVC()) {
        throw IllegalArgumentException("Start class ${args[0]} is not a ScreenVC !")
    }

    val rootPath = Paths.get(args[4])
    println("########   rootPath $rootPath")

    val feature =
        if (args[5] != "null") {
            println("########   feature = ${args[5]}")
            args[5]
        } else {
            null
        }

    val argBaseActVar = args[6]
    val baseActivityVar =
        if (argBaseActVar != "null") {
            println("########   baseActivityVar = ${argBaseActVar}")
            if (argBaseActVar.startsWith(".")) {
                "$appPackage$argBaseActVar"
            }
            else {
                argBaseActVar
            }
        } else {
            null
        }

    val initPlans =
        if (args[7] == "null") {
            emptyList<InitializationPlan>()
        } else {
            args[7].split("_").filterNot { it.isBlank() }
                .map { Class.forName(it).kotlin.createInstance() as InitializationPlan }
        }


    return Generator(
        appPackage,
        startClass as KClass<SKScreenVC>,
        rootStateClass,
        baseActivity,
        rootPath,
        feature,
        baseActivityVar,
        initializationPlans = initPlans,
        iOs = args[8].toBoolean(),
        referenceIconsByVariant = args[9].toBoolean()
    )

}

@ExperimentalStdlibApi
fun main(args: Array<String>) {
    buildGenerator(args).generate()
}