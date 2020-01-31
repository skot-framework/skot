package tech.skot.components

interface PagerView : ComponentView {
    val screens:List<ScreenView>

    var selectedPageIndex: Int
}