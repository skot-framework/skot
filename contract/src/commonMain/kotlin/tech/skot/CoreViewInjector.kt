package tech.skot

import tech.skot.components.PagerView
import tech.skot.components.ScreenView
import tech.skot.components.WebView

interface CoreViewInjector {

    fun web(redirect: List<WebView.RedirectParam>, userAgent:String?, onCantBack:(()->Unit)?): WebView

    fun pager(screens: List<ScreenView>, selectedPageIndex: Int): PagerView

}
