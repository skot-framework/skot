package tech.skot.core.components

class SKPagerWithTabsViewMock(
    pager: SKPagerVC, labels: List<String>
): SKComponentViewMock(), SKPagerWithTabsVC {
    override val pager: SKPagerVC = pager
    override var labels: List<String> = labels
}