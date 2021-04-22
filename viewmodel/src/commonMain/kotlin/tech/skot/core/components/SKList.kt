package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

open class SKList(vertical:Boolean = true, reverse:Boolean = false, nbColumns:Int? = null):SKComponent<SKListVC>() {
    override val view = coreViewInjector.skList(vertical, reverse, nbColumns)

    var items: List<SKComponent<*>> = emptyList()
        set(value) {
            view.items = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

}