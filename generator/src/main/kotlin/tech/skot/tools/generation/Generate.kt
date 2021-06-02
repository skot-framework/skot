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

    Generator(appPackage, startClass as KClass<SKScreenVC>, rootStateClass, baseActivity, rootPath)
            .generate()
}