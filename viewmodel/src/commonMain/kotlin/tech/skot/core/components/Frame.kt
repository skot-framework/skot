package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class Frame(val screens: Set<Screen<*>>, screenInitial: Screen<*>? = null) : Component<FrameVC>() {

    override val view = coreViewInjector.frame(screens = screens.map { it.view }.toSet(), screenInitial = screenInitial?.view)

    var screen: Screen<*>? = null
        set(value) {
            field = value
            view.screen = value?.view
        }
}