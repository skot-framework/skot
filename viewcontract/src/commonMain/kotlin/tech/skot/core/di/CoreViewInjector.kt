package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.presented.*

interface CoreViewInjector {
    fun rootStack(): SKStackVC
    fun stack(): SKStackVC
    fun alert(): SKAlertVC
    fun snackBar(): SKSnackBarVC
    fun bottomSheet(): SKBottomSheetVC
    fun pager(screens:List<SKScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int): SKPagerVC
    fun skList(vertical:Boolean, reverse:Boolean, nbColumns:Int?): SKListVC
    fun webView(config: SKWebViewVC.Config, openUrlInitial: SKWebViewVC.OpenUrl?): SKWebViewVC
    fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?): SKFrameVC
    fun loader(): SKLoaderVC
}
