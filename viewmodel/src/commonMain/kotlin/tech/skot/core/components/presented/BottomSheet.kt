package tech.skot.core.components.presented

import tech.skot.core.components.Component
import tech.skot.core.components.Screen
import tech.skot.core.di.coreViewInjector

class BottomSheet : Component<BottomSheetVC>() {

    override val view = coreViewInjector.bottomSheet()

    var shownScreen:Screen<*>? = null

    fun show(screen: Screen<*>) {
        shownScreen?.presenter = null
        view.state = BottomSheetVC.Shown(screen = screen.view)
        screen.presenter = this
        shownScreen = screen
    }

    fun dismiss() {
        shownScreen?.presenter = null
        shownScreen = null
        view.state = null
    }
}