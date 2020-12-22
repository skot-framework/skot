package tech.skot.core.components.presented

import tech.skot.core.components.ComponentView

interface AlertView : ComponentView {

    data class Shown(
            val title:String?,
            val message:String?,
            val onDismissRequest:(()->Unit)?,
            val buttons:List<Button>
    )

    data class Button(val label:String, val action:()->Unit)

    var state: Shown?
}