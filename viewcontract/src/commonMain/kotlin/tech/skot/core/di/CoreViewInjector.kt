package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.*

interface CoreViewInjector {
    fun rootStack(): StackVC
    fun stack(): StackVC
    fun alert(): AlertVC
    fun snackBar(): SnackBarVC
    fun bottomSheet(): BottomSheetVC
//    fun pager(screens:List<ScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int): PagerView
}
