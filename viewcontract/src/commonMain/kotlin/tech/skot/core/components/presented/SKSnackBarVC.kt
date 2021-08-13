package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsRoot

@SKLayoutIsRoot
interface SKSnackBarVC:SKComponentVC {

    data class Shown(
            val message:String,
            val action: Action?,
            val onTop:Boolean = false
    )
    data class Action(val label:String, val action:()->Unit)

    var state: Shown?
}