package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

open class SKBox(items: List<SKComponent<*>>?) : SKComponent<SKBoxVC>() {
    override val view = coreViewInjector.skBox(items?.map { it.view } ?: emptyList(), hiddenInitial = null)

    var items: List<SKComponent<*>> = emptyList()
        set(value) {
            view.items = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

    var hidden:Boolean? = null
        set(value) {
            field = value
            view.hidden = value
        }
}