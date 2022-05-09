package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

/**
 * [SKComponent] used to show a list or a grid
 * @param vertical [Boolean] specify if the list should scroll vertically or horizontally, vertical by default
 * @param reverse [Boolean] direction of the list
 * @param animate [Boolean]
 * @param animateItem [Boolean]
 *
 */
open class SKList(
    layoutMode: SKListVC.LayoutMode = SKListVC.LayoutMode.Linear(true),
    reverse: Boolean = false,
    animate: Boolean = true,
    animateItem: Boolean = false
) : SKComponent<SKListVC>() {



    override val view = coreViewInjector.skList(layoutMode, reverse, animate, animateItem)

    var items: List<SKComponent<*>> = emptyList()
        set(value) {
            view.items = value.map { Triple(it.view, it.computeItemId(), it.onSwipe) }
            field.forEach { if (!value.contains(it)) it.onRemove() }
            field = value
        }

    fun scrollToPosition(position: Int) {
        view.scrollToPosition(position)
    }

    fun showAll(item: SKComponent<*>) {
        view.scrollToPosition(items.indexOf(item))
    }

    override fun onRemove() {
        super.onRemove()
        items.forEach {
            it.onRemove()
        }
    }
}