package tech.skot.core.components

import android.webkit.WebView
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData
import tech.skot.view.live.SKMessage
import tech.skot.viewlegacy.R

class SKWebViewViewProxy(
    override val config: SKWebViewVC.Config = SKWebViewVC.Config(null),
    launchInitial: SKWebViewVC.Launch? = null
) : SKComponentViewProxy<WebView>(), SKWebViewVC {

    companion object {
        var LAYOUT_ID: Int? = null
    }

    override val layoutId: Int?
        get() = LAYOUT_ID ?: R.layout.sk_webview

    private val launchLD = MutableSKLiveData<SKWebViewVC.Launch?>(launchInitial)
    override var launch: SKWebViewVC.Launch? by launchLD

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
                launchLD.observe {
                    onLaunch(it)
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




