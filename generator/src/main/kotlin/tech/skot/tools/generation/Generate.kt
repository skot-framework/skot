package tech.skot.tools.generation

import java.nio.file.Paths
import tech.skot.core.components.SKScreenVC
import kotlin.reflect.KClass

@ExperimentalStdlibApi
fun main(args: Array<String>) {
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
    val rootPath = Paths.get(args[4])

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

    if (! startClass.isScreenVC()) {
        throw IllegalArgumentException("Start class ${args[0]} is not a ScreenVC !")
    }

    val feature =
    if (args[5] != "null") {
        println("########   feature = ${args[5]}")
        args[5]
    }
    else {
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
        }
        else {
            null
        }

    Generator(appPackage, startClass as KClass<SKScreenVC>, rootStateClass, baseActivity, rootPath, feature, baseActivityVar)
            .generate()
}