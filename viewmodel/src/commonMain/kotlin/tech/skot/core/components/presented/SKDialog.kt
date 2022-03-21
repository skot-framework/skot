package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.components.SKScreen
import tech.skot.core.di.coreViewInjector
import tech.skot.core.di.getByName
import tech.skot.core.view.Style

class SKDialog : SKComponent<SKDialogVC>() {

    override val view = coreViewInjector.dialog()

    var shownScreen: SKScreen<*>? = null


    fun show(
        screen: SKScreen<*>,
        cancelable: Boolean,
        onDismiss: (() -> Unit)? = null,
        style: Style? = null
    ) {
        shownScreen?.presenterDialog = null
        view.state = SKDialogVC.Shown(
            screen = screen.view,
            cancelable = cancelable,
            onDismiss = onDismiss,
            style
        )
        screen.presenterDialog = this
        shownScreen = screen
    }

    fun show(
        screen: SKScreen<*>,
        cancelable: Boolean,
        onDismiss: (() -> Unit)? = null,
        fullScreen: Boolean
    ) {
        shownScreen?.presenterDialog = null
        val style = if(fullScreen) fullScreenStyle else null
        view.state = SKDialogVC.Shown(
            screen = screen.view,
            cancelable = cancelable,
            onDismiss = onDismiss,
            style
        )
        screen.presenterDialog = this
        shownScreen = screen
    }


    fun dismiss() {
        shownScreen?.presenterDialog = null
        shownScreen = null
        view.state = null
    }

    companion object {
         val fullScreenStyle = getByName<Style>("skFullScreenDialogStyle")
    }
}