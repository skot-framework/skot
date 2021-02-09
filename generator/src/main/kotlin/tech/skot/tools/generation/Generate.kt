package tech.skot.tools.generation

import java.nio.file.Paths
import tech.skot.core.components.ComponentVC
import tech.skot.core.components.ScreenVC
import tech.skot.tools.generation.viewlegacy.skActivity
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
fun main(args: Array<String>) {
    val appPackage = args[0]
    val startClass = Class.forName(args[1]).kotlin
    val strBaseActivity = args[2]
    val rootPath = Paths.get(args[3])

    val baseActivity = if (strBaseActivity == "null") {
        null
    }
    else {
        strBaseActivity.fullNameAsClassName()
    }


    if (! startClass.isScreenVC()) {
        throw IllegalArgumentException("Start class ${args[0]} is not a ScreenVC !")
    }

    Generator(appPackage, startClass as KClass<ScreenVC>, baseActivity, rootPath)
            .generate()
}