package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo

@SKLayoutNo
interface SKAlertVC:SKComponentVC {

    data class Shown(
            val title:String?,
            val message:String?,
//            val onDismissRequest:(()->Unit)?,
            val cancelable:Boolean,
            val withInput:Boolean,
            val mainButton:Button,
            val secondaryButton:Button?
    )

    data class Button(val label:String, val action:(()->Unit)? = null)

    var state: Shown?
    var inputText:String?
}