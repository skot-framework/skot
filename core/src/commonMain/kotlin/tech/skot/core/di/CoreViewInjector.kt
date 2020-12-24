package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.AlertView
import tech.skot.core.components.presented.BottomSheetView
import tech.skot.core.components.presented.SnackBarView

interface CoreViewInjector {
    fun rootStack(bottomSheetView: BottomSheetView): RootStackView
    fun stack(): StackView
    fun alert(): AlertView
    fun snackBar(): SnackBarView
    fun bottomSheet(): BottomSheetView
    fun pager(screens:List<ScreenView>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int): PagerView
}

val coreViewInjector: CoreViewInjector by lazy {
    get<CoreViewInjector>()
}

val rootStack by lazy { RootStack() }