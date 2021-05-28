package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKListVC:SKComponentVC {
    var items:List<Pair<SKComponentVC, Any>>
}