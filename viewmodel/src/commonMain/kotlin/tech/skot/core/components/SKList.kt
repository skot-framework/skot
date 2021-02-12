package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

open class SKList(vertical:Boolean = true, reverse:Boolean = false):Component<SKListVC>() {
    override val view = coreViewInjector.skList(vertical, reverse)

    var items: List<Component<*>> = emptyList()
        set(value) {
            view.items = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

}