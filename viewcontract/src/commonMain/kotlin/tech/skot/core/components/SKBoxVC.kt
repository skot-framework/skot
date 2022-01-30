package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKBoxVC:SKComponentVC {
    var items:List<SKComponentVC>
    var hidden:Boolean?

    /**
     * when put in a SKBox or an SKList
     * true if you want vertical layout,
     * false (default value) if you want horizontal layout
     * no effect if your are not in a SKBox or SKList
     */
    val asItemVertical:Boolean?
}