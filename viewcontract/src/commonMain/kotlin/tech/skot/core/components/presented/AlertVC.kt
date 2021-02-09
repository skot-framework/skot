package tech.skot.core.components.presented

import tech.skot.core.components.ComponentVC
import tech.skot.core.components.NoLayout

@NoLayout
interface AlertVC:ComponentVC {

    data class Shown(
            val title:String?,
            val message:String?,
//            val onDismissRequest:(()->Unit)?,
            val mainButton:Button,
            val secondaryButton:Button?
    )

    data class Button(val label:String, val action:(()->Unit)?)

    var state: Shown?

}