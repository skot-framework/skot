package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKListVC:SKComponentVC {

    data class Item(val component:SKComponentVC,val id:Any, val onSwipe:(()->Unit)?, val spanSize:Int?)

    var items:List<Item>
    fun scrollToPosition(position:Int)
}