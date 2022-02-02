package tech.skot.core.components

class SKWebViewViewMock(config: SKWebViewVC.Config,
                        openUrlInitial: SKWebViewVC.OpenUrl?): SKComponentViewMock(), SKWebViewVC {
    override val config: SKWebViewVC.Config = config
    override var openUrl: SKWebViewVC.OpenUrl? = openUrlInitial
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