package tech.skot.core.components.presented

import tech.skot.core.components.ComponentView
import tech.skot.core.components.ScreenView

interface BottomSheetView : ComponentView {

    data class Shown(val screen:ScreenView)
    var state :Shown?

    fun onDismiss() {
        state = null
    }
}