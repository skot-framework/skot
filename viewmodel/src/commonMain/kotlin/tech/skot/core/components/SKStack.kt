package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.di.coreViewInjector

open class SKStack : SKComponent<SKStackVC>(), SKScreenParent {
    override val view = coreViewInjector.stack()

    var screens: List<SKScreen<*>> = emptyList()
        set(value) {
            view.screens = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            value.forEach { it.parent = this }
            field = value
        }

    var content: SKScreen<*>
        get() = screens.last()
        set(value) {
            SKLog.d("Stack will set screens to $value")
            screens = listOf(value)
        }

    override fun push(screen: SKScreen<*>) {
        SKLog.d("Will push screen: ${screen::class.simpleName}")
        screens += screen

    }

    fun pop(ifRoot:(()->Unit)? = null) {
        if (screens.size>1) {
            screens = screens - screens.last()
        }
        else {
            ifRoot?.invoke()
        }

    }

    override fun remove(screen: SKScreen<*>) {
        if (screens.contains(screen)) {
            screens = screens - screen
        }
    }

    override fun onRemove() {
        super.onRemove()
        screens.forEach { it.onRemove() }
    }


}