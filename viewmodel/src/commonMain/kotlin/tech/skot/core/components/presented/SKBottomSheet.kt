package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.components.SKScreen
import tech.skot.core.di.coreViewInjector

class SKBottomSheet : SKComponent<SKBottomSheetVC>() {

    override val view = coreViewInjector.bottomSheet()

    var shownScreen: SKScreen<*>? = null

    fun show(
        screen: SKScreen<*>,
        onDismiss: (() -> Unit)? = null,
        expanded: Boolean = true,
        skipCollapsed: Boolean = true,
        fullHeight: Boolean = false
    ) {
        shownScreen?.presenterBottomSheet = null
        view.state =
            SKBottomSheetVC.Shown(
                screen = screen.view,
                onDismiss = onDismiss,
                expanded = expanded,
                skipCollapsed = skipCollapsed,
                fullHeight = fullHeight
            )
        screen.presenterBottomSheet = this
        shownScreen = screen
    }

    fun dismiss() {
        shownScreen?.presenterBottomSheet = null
        shownScreen = null
        view.state = null
    }
}