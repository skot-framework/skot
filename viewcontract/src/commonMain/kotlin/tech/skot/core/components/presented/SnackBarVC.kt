package tech.skot.core.components.presented

import tech.skot.core.components.ComponentVC
import tech.skot.core.components.SKLayoutIsRoot

@SKLayoutIsRoot
interface SnackBarVC:ComponentVC {

    data class Shown(
            val message:String,
            val action: Action?
    )
    data class Action(val label:String, val action:()->Unit)

    var state: Shown?
}