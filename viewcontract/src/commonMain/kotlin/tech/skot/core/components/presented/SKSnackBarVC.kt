package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsRoot
import tech.skot.core.view.Color
import tech.skot.core.view.Icon

@SKLayoutIsRoot
interface SKSnackBarVC:SKComponentVC {

    data class Shown(
            val message:String,
            val action: Action? = null,
            val onTop:Boolean = false,
            val backgroundColor:Color? = null,
            val leftIcon:Icon? = null,
            val rightIcon:Icon? = null
    )
    data class Action(val label:String, val action:()->Unit)

    var state: Shown?
}