package tech.skot.core.components

import android.webkit.WebView
import androidx.fragment.app.Fragment
import tech.skot.view.live.MutableSKLiveData

class WebViewViewProxy(
        override val config: WebViewVC.Config,
        openUrlInitial: WebViewVC.OpenUrl?
) : ComponentViewProxy<WebView>(), WebViewVC {

    private val openUrlLD = MutableSKLiveData<WebViewVC.OpenUrl?>(openUrlInitial)
    override var openUrl: WebViewVC.OpenUrl? by openUrlLD

    private val goBackLD = MutableSKLiveData<WebViewVC.BackRequest?>(null)
    override var goBack: WebViewVC.BackRequest? by goBackLD


    override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: WebView, collectingObservers: Boolean) =
            WebViewView(activity, fragment, binding, this).apply {
                onConfig(config)
                openUrlLD.observe {
                    onOpenUrl(it)
                }
                goBackLD.observe {
                    onGoBackLD(it)
                }
            }

}




