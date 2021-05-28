package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.SKAlertViewProxy
import tech.skot.core.components.presented.SKBottomSheetViewProxy
import tech.skot.core.components.presented.SKSnackBarViewProxy


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack() = SKRootStackViewProxy

    override fun stack() = SKStackViewProxy()

    override fun alert() = SKAlertViewProxy()

    override fun snackBar() = SKSnackBarViewProxy()

    override fun bottomSheet() = SKBottomSheetViewProxy()

    override fun pager(screens:List<SKScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int) = SKPagerViewProxy(screens = screens as List<SKScreenViewProxy<*>>, onSwipeToPage = onSwipeToPage, initialSelectedPageIndex = initialSelectedPageIndex)

    override fun skList(vertical:Boolean, reverse:Boolean, nbColumns:Int?, animate:Boolean, animateItem:Boolean)  = SKListViewProxy(vertical, reverse, nbColumns, animate, animateItem)

    override fun webView(config: SKWebViewVC.Config, openUrlInitial: SKWebViewVC.OpenUrl?) = SKWebViewViewProxy(config, openUrlInitial)

    override fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?) = SKFrameViewProxy(screens = screens as Set<SKScreenViewProxy<*>>, screenInitial = screenInitial as SKScreenViewProxy<*>?)

    override fun loader() = SKLoaderViewProxy()
}

val coreViewModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}