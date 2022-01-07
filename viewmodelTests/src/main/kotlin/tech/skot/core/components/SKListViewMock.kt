package tech.skot.core.components

class SKListViewMock(
    vertical: Boolean,
    reverse: Boolean,
    nbColumns: Int?,
    animate: Boolean,
    animateItem: Boolean
): SKComponentViewMock(), SKListVC {
    override var items: List<Triple<SKComponentVC, Any, (() -> Unit)?>> = emptyList()

    override fun scrollToPosition(position: Int) {
    }
}

fun SKListVC.itemsComponents():List<SKComponentVC> = items.map { it.first }