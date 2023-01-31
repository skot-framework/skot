package tech.skot.core.components

import tech.skot.core.di.coreViewInjector

class SKWebView(config: SKWebViewVC.Config, launch: SKWebViewVC.Launch?) :
    SKComponent<SKWebViewVC>() {

    constructor(url: String) : this(SKWebViewVC.Config(null), SKWebViewVC.Launch.OpenUrl(url))

    override val view = coreViewInjector.webView(config, launch)

    fun back(onCantBack: (() -> Unit)? = null) {
        view.goBack = SKWebViewVC.BackRequest(onCantBack)
    }

    fun forward() {
        view.requestGoForward()
    }

    fun reload() {
        view.requestReload()
    }
 }