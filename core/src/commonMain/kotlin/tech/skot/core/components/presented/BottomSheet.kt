package tech.skot.core.components.presented

import tech.skot.core.components.Component
import tech.skot.core.components.Screen
import tech.skot.core.di.get

class BottomSheet : Component<BottomSheetView>() {

    override val view = get<BottomSheetView>()


    fun show(screen: Screen<*>) {
        view.state = BottomSheetView.Shown(screen = screen.view)
    }

    fun dismiss() {
        view.state = null
    }
}