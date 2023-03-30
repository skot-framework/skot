package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKPagerVC: SKComponentVC {
    var screens:List<SKScreenVC>
    var selectedPageIndex:Int
    val onUserSwipeToPage:((index:Int)->Unit)?
    val swipable:Boolean
}