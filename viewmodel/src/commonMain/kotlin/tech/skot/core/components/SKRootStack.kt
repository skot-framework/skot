package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

object SKRootStack:SKStack() {
    override val view = coreViewInjector.rootStack()

    fun resetToRoot() {
        state.screens.firstOrNull()?.removeAllScreensOnTop()
    }
}