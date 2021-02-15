package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class WebView(config: WebViewVC.Config, openUrl: WebViewVC.OpenUrl?) : Component<WebViewVC>() {

    constructor(url: String) : this(WebViewVC.Config(null, emptyList()), WebViewVC.OpenUrl(url))

    override val view = coreViewInjector.webView(config, openUrl)

    fun back() {
        view.goBack = WebViewVC.BackRequest()
    }
}