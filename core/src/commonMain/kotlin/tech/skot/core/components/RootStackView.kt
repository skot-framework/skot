package tech.skot.core.components

import tech.skot.core.components.ComponentView
import tech.skot.core.components.ScreenView

interface RootStackView : ComponentView {
    var screens: List<ScreenView>
//    fun setRootScreen(aScreen:ScreenView)
}