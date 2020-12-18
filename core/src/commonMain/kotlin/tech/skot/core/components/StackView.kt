package tech.skot.core.components

import tech.skot.core.components.ComponentView
import tech.skot.core.components.ScreenView

interface StackView : ComponentView {
    var screens: List<ScreenView>
}