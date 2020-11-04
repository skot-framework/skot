package tech.skot.components

import android.net.Uri
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import tech.skot.core.SKLog
import java.net.URLEncoder

class WebViewImpl(
        redirect: List<WebView.RedirectParam>,
        override val userAgent: String? = null
) : WebViewImplGen(redirect) {

    var redirectParams: List<WebView.RedirectParam> = emptyList()

    override fun onInflated() {
        super.onInflated()
        binding.let {webView ->
            webView.settings.apply {
                javaScriptEnabled = true
                userAgent?.let { userAgentString = it }
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, request: WebResourceRequest?): Boolean {
                        request?.url?.toString()?.let { url ->
                            SKLog.d("redirect -----> $url")
                            redirectParams.forEach {
                                if (it.matches(url)) {
                                    return it.onRedirect(url, request.url?.getMapQueryParameters()
                                            ?: emptyMap())
                                }
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        url?.let { loadedUrl ->
                            requiredUrl?.let {
                                onFinished?.invoke()
                                onFinished = null
                                javascriptOnFinished?.let {
                                    webView.evaluateJavascript(it, null)
                                    javascriptOnFinished = null
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

            } else {
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, url: String?): Boolean {
                        Uri.parse(url)?.let { uri ->
                            val strUrl = uri.toString()
                            redirectParams.forEach {
                                if (it.matches(strUrl)) {
                                    return it.onRedirect(strUrl, uri.getMapQueryParameters())
                                }
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, url)
                    }

                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        url?.let { loadedUrl ->
                            requiredUrl?.let {
                                onFinished?.invoke()
                                onFinished = null
                                javascriptOnFinished?.let {
                                    webView.evaluateJavascript(it, null)
                                    javascriptOnFinished = null
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
    private var javascriptOnFinished: String? = null

    private var onError: (() -> Unit)? = null
    override fun openUrlNow(url: String, onFinished: (() -> Unit)?, javascriptOnFinished: String?, onError: (() -> Unit)?, post: Map<String, String>?) {
        requiredUrl = url
        this.onFinished = onFinished
        this.javascriptOnFinished = javascriptOnFinished
        this.onError = onError
        if (post != null) {
            val params = post.map {
                "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}"
            }
                    .joinToString(separator = "&")
            binding.postUrl(url, params.toByteArray())
        } else {
            binding.loadUrl(url)
        }

    }

}