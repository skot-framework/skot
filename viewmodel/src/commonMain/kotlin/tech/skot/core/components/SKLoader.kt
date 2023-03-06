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

    var onStateChanged:((visible:Boolean)->Unit)? = null

    fun isLoading():Boolean = loadingCounter > 0
    fun isNotLoading():Boolean = !isLoading()

    private var loadingCounter = 0
        set(value) {
            if (field > 0 && value <= 0 || field <=0 && value > 0) {
                onStateChanged?.invoke(value > 0)
            }
            field = value
            view.visible = value > 0
        }
}