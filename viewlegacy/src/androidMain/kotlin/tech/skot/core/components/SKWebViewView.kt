package tech.skot.core.components

import android.net.Uri
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import java.net.URLEncoder

class SKWebViewView(
    override val proxy: SKWebViewViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val webView: WebView,
) : SKComponentView<WebView>(proxy, activity, fragment, webView) {

    fun onConfig(config: SKWebViewVC.Config) {
        webView.settings.apply {
            userAgentString = config.userAgent
            javaScriptEnabled = true
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: android.webkit.WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.let {
                        if (it.isForMainFrame && it.isRedirect) {
                            oneRedirectionAskedForCurrentOpenUrl = true
                        }
                    }
                    request?.url?.toString()?.let { url ->
                        config.redirect.forEach {
                            if (it.matches(url)) {
                                return it.onRedirect(
                                    url, request.url?.getMapQueryParameters()
                                        ?: emptyMap()
                                )
                            }
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    openingUrl?.finished(url)
                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    openingUrl?.error(request?.url)
                    super.onReceivedError(view, request, error)
                }
            }

        } else {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    oneRedirectionAskedForCurrentOpenUrl = true
                    Uri.parse(url)?.let { uri ->
                        val strUrl = uri.toString()
                        config.redirect.forEach {
                            if (it.matches(strUrl)) {
                                return it.onRedirect(strUrl, uri.getMapQueryParameters())
                            }
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    openingUrl?.finished(url)
                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    openingUrl?.error(request?.url)
                    super.onReceivedError(view, request, error)
                }
            }
        }

    }

    private var openingUrl: SKWebViewVC.OpenUrl? = null

    private var oneRedirectionAskedForCurrentOpenUrl = false

    private fun SKWebViewVC.OpenUrl.finished(finishedUrl: String?) {
        if (finishedUrl == url || oneRedirectionAskedForCurrentOpenUrl) {
            openingUrl = null
            onFinished?.invoke()
            javascriptOnFinished?.let {
                webView.evaluateJavascript(it, null)
            }

        }

    }

    private fun SKWebViewVC.OpenUrl.error(requestedUri: Uri?) {
        requestedUri?.toString()?.let { requestUrl ->
            if (url == requestUrl) {
                onError?.invoke()
                openingUrl = null
            }
        }
    }

    fun onOpenUrl(openUrl: SKWebViewVC.OpenUrl?) {
        oneRedirectionAskedForCurrentOpenUrl = false
        openingUrl = openUrl
        if (openUrl != null) {
            val posts = openUrl.post
            if (posts != null) {
                val params = posts.map {
                    "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}"
                }
                    .joinToString(separator = "&")
                binding.postUrl(openUrl.url, params.toByteArray())
            } else {
                binding.loadUrl(openUrl.url)
            }
        }

    }

    fun onGoBackLD(goBack: SKWebViewVC.BackRequest?) {
        goBack?.let {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                it.onCantBack?.invoke()
            }
            proxy.goBack = null
        }

    }

    fun onRequestGoForward() {
        webView.goForward()
    }

    fun onRequestReload() {
        webView.reload()
    }

    fun Uri.getMapQueryParameters(): Map<String, String> =
        try {
            queryParameterNames.map { it to getQueryParameter(it)!! }.toMap()
        } catch (ex: Exception) {
            SKLog.e(ex, "Pb au parse des paramètres d'une url de redirection")
            emptyMap()
        }


}