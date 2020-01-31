package tech.skot.components


class Pager(private val screens: List<Screen<*>>, intialSelectedPageIndex: Int = 0) : Component<PagerView>() {
    override val view: PagerView = coreViewInjector.pager(
            screens.map { it.view },
            intialSelectedPageIndex
    )

    override fun onRemove() {
        screens.forEach { it.onRemove() }
        super.onRemove()
    }
}
