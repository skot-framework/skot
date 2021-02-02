package tech.skot.core.di

import tech.skot.core.components.RootStackViewProxy


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack() = RootStackViewProxy()

    override fun stack() = StackViewProxy()

    override fun alert() = AlertViewProxy()

    override fun snackBar() = SnackBarViewProxy()

    override fun bottomSheet() = BottomSheetViewProxy()

    override fun pager(screens:List<ScreenView>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int) = PagerViewProxy(screens = screens as List<ScreenViewProxy<*>>, onSwipeToPage = onSwipeToPage, initialSelectedPageIndex = initialSelectedPageIndex)
}