package tech.skot.core.components

import tech.skot.core.components.presented.BottomSheetView

interface RootStackView : ComponentView {
    val bottomSheet:BottomSheetView
    var screens: List<ScreenView>
//    fun setRootScreen(aScreen:ScreenView)
}