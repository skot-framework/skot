package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo
import tech.skot.core.components.SKScreenVC
import tech.skot.core.view.Style

@SKLayoutNo
interface SKDialogVC: SKComponentVC {

    data class Shown(val screen: SKScreenVC, val cancelable:Boolean, val onDismiss:(()->Unit)? = null, val style : Style? = null)

    var state :Shown?
}