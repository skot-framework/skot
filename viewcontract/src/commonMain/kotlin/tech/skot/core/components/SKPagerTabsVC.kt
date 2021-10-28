package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKPagerWithTabsVC: SKComponentVC {
    val pager:SKPagerVC
    var labels:List<String>
}