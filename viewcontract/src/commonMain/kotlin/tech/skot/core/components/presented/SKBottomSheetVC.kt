package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutNo
import tech.skot.core.components.SKScreenVC

@SKLayoutNo
interface SKBottomSheetVC : SKComponentVC {

    sealed class PeekHeight(val value : Int)
    object PeekHeightAuto : PeekHeight(-1)
    class PeekHeightSize(size: Int) : PeekHeight(size)

    data class Shown(
        val screen: SKScreenVC,
        val onDismiss: (() -> Unit)? = null,
        val expanded: Boolean = true,
        val skipCollapsed: Boolean = true,
        val hideable : Boolean = true,
        val peekHeight: PeekHeight = PeekHeightAuto
    )

    var state: Shown?


}