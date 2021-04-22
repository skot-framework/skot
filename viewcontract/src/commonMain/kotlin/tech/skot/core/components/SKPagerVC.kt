package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKPagerVC: SKComponentVC {
    val screens:List<SKScreenVC>
    var selectedPageIndex:Int
    val onSwipeToPage:((index:Int)->Unit)?
}