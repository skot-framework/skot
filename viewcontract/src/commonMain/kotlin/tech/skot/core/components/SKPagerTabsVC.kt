package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKPagerWithTabsVC: SKComponentVC {
    val pager:SKPagerVC
    val labels:List<String>
}