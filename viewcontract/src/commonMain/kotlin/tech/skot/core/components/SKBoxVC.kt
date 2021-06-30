package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKBoxVC:SKComponentVC {
    var items:List<SKComponentVC>
    var hidden:Boolean?
}