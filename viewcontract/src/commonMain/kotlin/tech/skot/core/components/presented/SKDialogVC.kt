package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo
import tech.skot.core.components.SKScreenVC

@SKLayoutNo
interface SKDialogVC: SKComponentVC {

    data class Shown(val screen: SKScreenVC, val cancelable:Boolean, val onDismiss:(()->Unit)? = null)

    var state :Shown?
}