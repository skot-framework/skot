package tech.skot.core.components.presented

import tech.skot.core.components.Component
import tech.skot.core.components.Screen
import tech.skot.core.di.coreViewInjector
import tech.skot.core.di.get

class BottomSheet : Component<BottomSheetView>() {

    override val view = coreViewInjector.bottomSheet()

    var shownScreen:Screen<*>? = null

    fun show(screen: Screen<*>) {
        shownScreen?.presenter = null
        view.state = BottomSheetView.Shown(screen = screen.view)
        screen.presenter = this
        shownScreen = screen
    }

    fun dismiss() {
        shownScreen?.presenter = null
        shownScreen = null
        view.state = null
    }
}