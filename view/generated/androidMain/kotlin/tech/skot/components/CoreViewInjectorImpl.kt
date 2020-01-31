package tech.skot.components

import tech.skot.CoreViewInjector

class CoreViewInjectorImpl : CoreViewInjector {
    override fun web(redirect: List<WebView.RedirectParam>) = WebViewImpl(redirect)
    override fun pager(screens: List<ScreenView>, selectedPageIndex:Int) = PagerViewImpl(screens, selectedPageIndex)
}