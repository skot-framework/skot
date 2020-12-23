package tech.skot.core.components.presented

import tech.skot.core.components.ComponentView

interface AlertView : ComponentView {

    data class Shown(
            val title:String?,
            val message:String?,
//            val onDismissRequest:(()->Unit)?,
            val mainButton:Button,
            val secondaryButton:Button?
    )

    fun onDismiss() {
        state = null
    }

    data class Button(val label:String, val action:(()->Unit)?)

    var state: Shown?
}