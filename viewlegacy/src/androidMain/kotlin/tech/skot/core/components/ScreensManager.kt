package tech.skot.core.components

import tech.skot.view.legacy.ScreenViewProxy
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage


object ScreensManager {

    private var counter: Long = 0
    private val instances: MutableMap<Long, ScreenViewProxy<*>> = mutableMapOf()

    fun getInstance(key: Long): ScreenViewProxy<*>? = instances.get(key)

    fun addScreen(screen: ScreenViewProxy<*>):Long {
        val key = counter ++
        instances[key] = screen
        return key
    }

    val backPressed = SKMessage<Unit>(true)

    const val SK_EXTRA_VIEW_KEY = "SK_EXTRA_VIEW_KEY"
    const val SK_ARGUMENT_VIEW_KEY = "SK_ARGUMENT_VIEW_KEY"
}