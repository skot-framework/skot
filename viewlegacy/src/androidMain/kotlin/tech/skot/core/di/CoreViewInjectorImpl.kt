package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.AlertViewProxy
import tech.skot.core.components.presented.BottomSheetViewProxy
import tech.skot.core.components.presented.SKSnackBarViewProxy


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack() = RootStackViewProxy

    override fun stack() = StackViewProxy()

    override fun alert() = AlertViewProxy()

    override fun snackBar() = SKSnackBarViewProxy()

    override fun bottomSheet() = BottomSheetViewProxy()

    override fun pager(screens:List<ScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int) = PagerViewProxy(screens = screens as List<ScreenViewProxy<*>>, onSwipeToPage = onSwipeToPage, initialSelectedPageIndex = initialSelectedPageIndex)

    override fun skList(vertical:Boolean, reverse:Boolean, nbColumns:Int?) = SKListViewProxy(vertical, reverse, nbColumns)

    override fun webView(config: WebViewVC.Config, openUrlInitial: WebViewVC.OpenUrl?) = WebViewViewProxy(config, openUrlInitial)

    override fun frame(screens: Set<ScreenVC>, screenInitial: ScreenVC?) = FrameViewProxy(screens = screens as Set<ScreenViewProxy<*>>, screenInitial = screenInitial as ScreenViewProxy<*>?)

    override fun loader() = LoaderViewProxy()
}

val coreViewModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}