package tech.skot.core.components

import android.webkit.WebView
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewlegacy.R

class SKWebViewViewProxy(
    override val config: SKWebViewVC.Config = SKWebViewVC.Config(null, null, null),
    openUrlInitial: SKWebViewVC.OpenUrl? = null
) : SKComponentViewProxy<WebView>(), SKWebViewVC {

    companion object {
        var LAYOUT_ID: Int? = null
    }

    override val layoutId: Int?
        get() = LAYOUT_ID ?: R.layout.sk_webview

    private val openUrlLD = MutableSKLiveData<SKWebViewVC.OpenUrl?>(openUrlInitial)
    override var openUrl: SKWebViewVC.OpenUrl? by openUrlLD

    private val goBackLD = MutableSKLiveData<SKWebViewVC.BackRequest?>(null)
    override var goBack: SKWebViewVC.BackRequest? by goBackLD

    private val requestGoForwardMessage = SKMessage<Unit>()
    override fun requestGoForward() {
        requestGoForwardMessage.post(Unit)
    }

    private val requestReloadMessage = SKMessage<Unit>()
    override fun requestReload() {
        requestReloadMessage.post(Unit)
    }

    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: WebView) =
            SKWebViewView(this, activity, fragment, binding).apply {
                onConfig(config)
                openUrlLD.observe {
                    onOpenUrl(it)
                }
                goBackLD.observe {
                    onGoBackLD(it)
                }
                requestGoForwardMessage.observe {
                    onRequestGoForward()
                }
                requestReloadMessage.observe {
                    onRequestReload()
                }
            }

}




