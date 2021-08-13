package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.di.coreViewInjector

class SKFrame(val screens: Set<SKScreen<*>>, screenInitial: SKScreen<*>? = null) : SKComponent<SKFrameVC>() {

    override val view = coreViewInjector.frame(screens = screens.map { it.view }.toSet(), screenInitial = screenInitial?.view)

    var screen: SKScreen<*>? = null
        set(value) {
            SKLog.d("@@@@@@@ SKFrame will set screen: $value")
            field = value
            view.screen = value?.view
        }
}