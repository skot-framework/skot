package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKListVC:SKComponentVC {
    var items:List<Triple<SKComponentVC, Any, (()->Unit)?>>
    fun scrollToPosition(position:Int)
}