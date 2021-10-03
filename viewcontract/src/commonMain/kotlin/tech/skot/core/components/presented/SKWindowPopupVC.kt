package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo

@SKLayoutNo
interface SKWindowPopupVC : SKComponentVC {

    data class Shown(
        val component: SKComponentVC,
        val behavior : Behavior
    )


    sealed class Behavior
    class Cancelable(val onDismiss:(()->Unit)? = null) : Behavior()
    object NotCancelable : Behavior()


    var state: Shown?

}