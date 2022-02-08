package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo

@SKLayoutNo
interface SKAlertVC:SKComponentVC {

    data class Shown(
            val title:String? = null,
            val message:String? = null,
//            val onDismissRequest:(()->Unit)?,
            val cancelable:Boolean = false,
            val withInput:Boolean = false,
            val mainButton:Button,
            val secondaryButton:Button? = null
    )

    data class Button(val label:String, val action:(()->Unit)? = null)

    var state: Shown?
    var inputText:String?
}