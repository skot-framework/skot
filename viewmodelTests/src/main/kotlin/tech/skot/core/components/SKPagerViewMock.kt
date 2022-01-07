package tech.skot.core.components

class SKPagerViewMock(
    screens: List<SKScreenVC>,
    onSwipeToPage: ((index: Int) -> Unit)?,
    initialSelectedPageIndex: Int,
    swipable: Boolean
): SKComponentViewMock(), SKPagerVC {
    override var screens: List<SKScreenVC> = screens
    override var selectedPageIndex: Int = initialSelectedPageIndex
    override val onSwipeToPage = onSwipeToPage
    override val swipable: Boolean = swipable

    fun userSwipeToPage(index:Int) {
        onSwipeToPage?.invoke(index)
    }
}

fun SKPagerVC.userSwipeToPage(index:Int) {
    (this as SKPagerViewMock).userSwipeToPage(index)
}