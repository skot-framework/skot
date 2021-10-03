package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

class SKWindowPopup : SKComponent<SKWindowPopupVC>() {

    override val view = coreViewInjector.windowPopup()

    var shownComponent: SKComponent<*>? = null


    fun show(component: SKComponent<*>,behavior: SKWindowPopupVC.Behavior ) {
        view.state = SKWindowPopupVC.Shown(component = component.view, behavior = behavior)
        shownComponent = component
    }

    fun dismiss() {
        shownComponent = null
        view.state = null
    }
}