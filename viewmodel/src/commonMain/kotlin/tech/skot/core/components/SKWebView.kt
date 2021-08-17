package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKWebView(config: SKWebViewVC.Config, openUrl: SKWebViewVC.OpenUrl?) :
    SKComponent<SKWebViewVC>() {

    constructor(url: String) : this(SKWebViewVC.Config(null, emptyList()), SKWebViewVC.OpenUrl(url))

    override val view = coreViewInjector.webView(config, openUrl)

    fun back(onCantBack: (() -> Unit)? = null) {
        view.goBack = SKWebViewVC.BackRequest(onCantBack)
    }
}