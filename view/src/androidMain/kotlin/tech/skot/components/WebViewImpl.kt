package tech.skot.components

import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import tech.skot.core.SKLog

class WebViewImpl(
        redirect: List<WebView.RedirectParam>
) : WebViewImplGen(redirect) {

    lateinit var webView: android.webkit.WebView
    var redirectParams: List<WebView.RedirectParam> = emptyList()

    override fun onInflated() {
        super.onInflated()
        webView = binding
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, request: WebResourceRequest?): Boolean {
                request?.url?.toString()?.let { url ->
                    redirectParams.forEach {
                        if (it.matches(url)) {
                            it.onRedirect(url, request.url?.getMapQueryParameters() ?: emptyMap())
                            return true
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                url?.let { loadedUrl ->
                    requiredUrl?.let {
                        if (it == loadedUrl) {
                            onFinished?.invoke()
                            onFinished = null
                        }
                    }
                }
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: android.webkit.WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                requiredUrl?.let {
                    request?.url?.toString()?.let { requestUrl ->
                        if (requiredUrl == requestUrl) {
                            onError?.invoke()
                            onError = null
                        }
                    }
                }
                super.onReceivedError(view, request, error)
            }
        }

    }

    fun Uri.getMapQueryParameters(): Map<String, String> =
            try {
                queryParameterNames.map { it to getQueryParameter(it)!! }.toMap()
            } catch (ex: Exception) {
                SKLog.e("Pb au parse des paramètres d'une url de redirection", ex)
                emptyMap()
            }


    override fun onRedirect(redirect: List<WebView.RedirectParam>) {
        redirectParams = redirect
    }

    private var requiredUrl: String? = null
    private var onFinished: (() -> Unit)? = null
    private var onError: (() -> Unit)? = null
    override fun openUrlNow(url: String, onFinished: (() -> Unit)?, onError: (() -> Unit)?) {
        requiredUrl = url
        this.onFinished = onFinished
        this.onError = onError
        webView.loadUrl(url)
    }

}