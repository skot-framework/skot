package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.*

interface CoreViewInjector {
    fun rootStack(): StackVC
    fun stack(): StackVC
    fun alert(): AlertVC
    fun snackBar(): SKSnackBarVC
    fun bottomSheet(): BottomSheetVC
    fun pager(screens:List<ScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int): PagerVC
    fun skList(vertical:Boolean, reverse:Boolean, nbColumns:Int?): SKListVC
    fun webView(config: WebViewVC.Config, openUrlInitial: WebViewVC.OpenUrl?): WebViewVC
    fun frame(screens: Set<ScreenVC>,screenInitial: ScreenVC?): FrameVC
    fun loader(): LoaderVC
}
