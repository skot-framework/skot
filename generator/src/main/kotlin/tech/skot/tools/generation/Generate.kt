package tech.skot.tools.generation

import java.nio.file.Paths
import tech.skot.core.components.ComponentVC
import tech.skot.core.components.ScreenVC
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
    val strBaseActivity = args[2]
    val rootPath = Paths.get(args[3])

    val baseActivity =
            when {
                strBaseActivity == "null" -> null
                strBaseActivity.startsWith(".") -> {
                    "$appPackage$strBaseActivity".fullNameAsClassName()
                }
                else -> {
                    strBaseActivity.fullNameAsClassName()
                }
            }


    if (! startClass.isScreenVC()) {
        throw IllegalArgumentException("Start class ${args[0]} is not a ScreenVC !")
    }

    Generator(appPackage, startClass as KClass<ScreenVC>, baseActivity, rootPath)
            .generate()
}