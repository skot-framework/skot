package tech.skot.tools.generation

import com.squareup.kotlinpoet.ClassName
import kotlinx.coroutines.GlobalScope

object FrameworkClassNames {
//    val icon = ClassName("")
    val coroutineScope = ClassName("kotlinx.coroutines", "CoroutineScope")
    val coroutineContext = ClassName("kotlin.coroutines", "CoroutineContext")

    val skManualData = ClassName("tech.skot.model","SKManualData")
    val skData = ClassName("tech.skot.model","SKData")
    val globalCache = ClassName("tech.skot.model","globalCache")
    val skActivity = ClassName("tech.skot.core.components","SKActivity")
    val get = ClassName("tech.skot.core.di","get")

    val globalScope = ClassName("kotlinx.coroutines","GlobalScope")
    val launch = ClassName("kotlinx.coroutines","launch")

    val mapSKData = ClassName("tech.skot.model", "map")
    val combineSKData = ClassName("tech.skot.model", "combineSKData")

    val bm = ClassName("tech.skot.model", "SKBM")

    val transistionAndroidLegacy = ClassName("tech.skot.view", "SKTransitionAndroidLegacy")
}