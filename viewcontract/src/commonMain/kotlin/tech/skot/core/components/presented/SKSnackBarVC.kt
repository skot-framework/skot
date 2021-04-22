package tech.skot.core.components.presented

import tech.skot.core.components.ComponentVC
import tech.skot.core.components.SKLayoutIsRoot
import tech.skot.core.components.SKPassToParentView

@SKPassToParentView
@SKLayoutIsRoot
interface SKSnackBarVC:ComponentVC {

    data class Shown(
            val message:String,
            val action: Action?,
            val onTop:Boolean = false
    )
    data class Action(val label:String, val action:()->Unit)

    var state: Shown?
}