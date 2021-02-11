package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

open class SKList:Component<SKListVC>() {
    override val view = coreViewInjector.skList()

    var items: List<Component<*>> = emptyList()
        set(value) {
            view.items = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

}