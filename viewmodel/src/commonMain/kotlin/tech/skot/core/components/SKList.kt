package tech.skot.core.components

import tech.skot.core.SKLog
import tech.skot.core.di.coreViewInjector

open class SKList(
    vertical: Boolean = true,
    reverse: Boolean = false,
    nbColumns: Int? = null,
    animate: Boolean = true,
    animateItem: Boolean = false
) : SKComponent<SKListVC>() {
    override val view = coreViewInjector.skList(vertical, reverse, nbColumns, animate, animateItem)

    var items: List<SKComponent<*>> = emptyList()
        set(value) {
            view.items = value.map { Triple(it.view, it.computeItemId(), it.onSwipe) }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

    fun scrollToPosition(position: Int) {
        view.scrollToPosition(position)
    }

    fun showAll(item:SKComponent<*>) {
        view.scrollToPosition(items.indexOf(item))
    }

    override fun onRemove() {
        super.onRemove()
        items.forEach {
            it.onRemove()
        }
    }
}