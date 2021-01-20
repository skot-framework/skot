package tech.skot.core.components

interface PagerView : ComponentView {
    val screens:List<ScreenView>
    var selectedPageIndex:Int
    val onSwipeToPage:((index:Int)->Unit)?
}