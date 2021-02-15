package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.AlertViewProxy
import tech.skot.core.components.presented.BottomSheetViewProxy
import tech.skot.core.components.presented.SnackBarViewProxy


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack() = RootStackViewProxy

    override fun stack() = StackViewProxy()

    override fun alert() = AlertViewProxy()

    override fun snackBar() = SnackBarViewProxy()

    override fun bottomSheet() = BottomSheetViewProxy()

    override fun pager(screens:List<ScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int) = PagerViewProxy(screens = screens as List<ScreenViewProxy<*>>, onSwipeToPage = onSwipeToPage, initialSelectedPageIndex = initialSelectedPageIndex)

    override fun skList(vertical:Boolean, reverse:Boolean) = SKListViewProxy(vertical, reverse)

    override fun webView(config: WebViewVC.Config, openUrlInitial: WebViewVC.OpenUrl?) = WebViewViewProxy(config, openUrlInitial)
}

val coreViewModuleInjector = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}