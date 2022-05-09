package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKListVC : SKComponentVC {

    sealed class LayoutMode {
        object Manual : LayoutMode()
        class Linear(val vertical:Boolean = true) : LayoutMode()
        class Grid(val nbComumns: Int, val vertical:Boolean  = true) : LayoutMode()
    }

    var items: List<Triple<SKComponentVC, Any, (() -> Unit)?>>
    fun scrollToPosition(position: Int)
}