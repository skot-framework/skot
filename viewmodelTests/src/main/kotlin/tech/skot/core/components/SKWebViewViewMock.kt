package tech.skot.core.components

class SKWebViewViewMock(config: SKWebViewVC.Config,
                        launchInitial: SKWebViewVC.Launch?): SKComponentViewMock(), SKWebViewVC {
    override val config: SKWebViewVC.Config = config
    override var launch: SKWebViewVC.Launch? = launchInitial
    override var goBack: SKWebViewVC.BackRequest? = null

    var requestGoForwardCount = 0
    override fun requestGoForward() {
        requestGoForwardCount++
    }

    var requestReloadCount = 0
    override fun requestReload() {
        requestReloadCount++
    }
}