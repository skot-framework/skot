package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

open class SKList(vertical:Boolean = true, reverse:Boolean = false, nbColumns:Int? = null, animate:Boolean = true, animateItem:Boolean = false):SKComponent<SKListVC>() {
    override val view = coreViewInjector.skList(vertical, reverse, nbColumns, animate, animateItem)

    var items: List<SKComponent<*>> = emptyList()
        set(value) {
            view.items = value.map { Pair(it.view, it.computeItemId()) }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }
}