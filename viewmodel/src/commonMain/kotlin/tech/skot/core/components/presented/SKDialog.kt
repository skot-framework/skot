package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.components.SKScreen
import tech.skot.core.di.coreViewInjector

class SKDialog : SKComponent<SKDialogVC>() {

    override val view = coreViewInjector.dialog()

    var shownScreen: SKScreen<*>? = null

    fun show(screen: SKScreen<*>, cancelable:Boolean, onDismiss: (() -> Unit)? = null) {
        shownScreen?.presenterDialog = null
        view.state = SKDialogVC.Shown(screen = screen.view, cancelable = cancelable, onDismiss = onDismiss)
        screen.presenterDialog = this
        shownScreen = screen
    }

    fun dismiss() {
        shownScreen?.presenterDialog = null
        shownScreen = null
        view.state = null
    }
}