package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo
import tech.skot.core.components.SKScreenVC

@SKLayoutNo
interface SKBottomSheetVC : SKComponentVC {

    data class Shown(
        val screen: SKScreenVC,
        val onDismiss: (() -> Unit)? = null,
        val expanded: Boolean = true,
        val skipCollapsed: Boolean = true,
        val fullHeight: Boolean = false
    )

    var state: Shown?


}