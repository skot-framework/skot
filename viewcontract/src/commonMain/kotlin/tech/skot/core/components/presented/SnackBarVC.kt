package tech.skot.core.components.presented

import tech.skot.core.components.ComponentVC
import tech.skot.core.components.LayoutIsRoot
import tech.skot.core.components.NoLayout

@LayoutIsRoot
interface SnackBarVC:ComponentVC {

    data class Shown(
            val message:String,
            val action: Action?
    )
    data class Action(val label:String, val action:()->Unit)

    var state: Shown?
}