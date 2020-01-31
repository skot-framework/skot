package tech.skot

import tech.skot.components.FrameView
import tech.skot.components.PagerView
import tech.skot.components.ScreenView
import tech.skot.components.WebView

interface CoreViewInjector {

    fun web(redirect: List<WebView.RedirectParam>): WebView

    fun pager(screens: List<ScreenView>, selectedPageIndex:Int): PagerView

}
