package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKLoader : SKComponent<SKLoaderVC>() {

    override val view = coreViewInjector.loader()

    fun workStarted() {
        loadingCounter++
    }

    fun workEnded() {
        loadingCounter--
    }

    fun isLoading():Boolean = loadingCounter > 0
    fun isNotLoading():Boolean = !isLoading()

    private var loadingCounter = 0
        set(value) {
            field = value
            view.visible = value > 0
        }
}