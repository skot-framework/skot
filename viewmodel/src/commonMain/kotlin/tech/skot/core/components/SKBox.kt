package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

open class SKBox(items: List<SKComponent<*>>?) : SKComponent<SKBoxVC>() {
    constructor(vararg item:SKComponent<*>) : this(item.asList())


    override val view = coreViewInjector.skBox(items?.map { it.view } ?: emptyList(), hiddenInitial = null)

    var items: List<SKComponent<*>> = emptyList()
        set(value) {
            view.items = value.map { it.view }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

    var content: SKComponent<*>? = null
        set(value) {
            field = value
            items = value?.let { listOf(it) } ?: emptyList()
        }


    var hidden:Boolean? = null
        set(value) {
            field = value
            view.hidden = value
        }
}