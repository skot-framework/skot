package tech.skot.core.components

import android.webkit.WebView
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class SKWebViewViewProxy(
        override val config: SKWebViewVC.Config,
        openUrlInitial: SKWebViewVC.OpenUrl?
) : SKComponentViewProxy<WebView>(), SKWebViewVC {

    private val openUrlLD = MutableSKLiveData<SKWebViewVC.OpenUrl?>(openUrlInitial)
    override var openUrl: SKWebViewVC.OpenUrl? by openUrlLD

    private val goBackLD = MutableSKLiveData<SKWebViewVC.BackRequest?>(null)
    override var goBack: SKWebViewVC.BackRequest? by goBackLD


    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: WebView, collectingObservers: Boolean) =
            SKWebViewView(this, activity, fragment, binding).apply {
                onConfig(config)
                openUrlLD.observe {
                    onOpenUrl(it)
                }
                goBackLD.observe {
                    onGoBackLD(it)
                }
            }

}




