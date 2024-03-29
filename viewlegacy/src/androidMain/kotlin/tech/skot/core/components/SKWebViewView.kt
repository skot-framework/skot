package tech.skot.core.components

import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import tech.skot.core.toSKUri
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
            javaScriptEnabled = config.javascriptEnabled
            domStorageEnabled = config.domStorageEnabled
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: android.webkit.WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.url?.toSKUri()?.let { skUri ->
                        try {
                            if (config.shouldOverrideUrlLoading?.invoke(skUri) == true) {
                                return true
                            }
                        } catch (ex: Exception) {
                            SKLog.e(
                                ex,
                                "Erreur dans l'invocation de shouldOverrideUrlLoading depuis SKWebViewView"
                            )
                        }

                        try {
                            if (activity.featureInitializer.onDeepLink?.invoke(skUri, true) == true
                            ) {
                                return true
                            }
                        } catch (ex: Exception) {
                            SKLog.e(
                                ex,
                                "Erreur dans l'invocation de onDeepLink depuis SKWebViewView"
                            )
                        }
                    }
                    request?.let {
                        if (it.isForMainFrame && it.isRedirect) {
                            oneRedirectionAskedForCurrentOpenUrl = true
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    openingUrl?.finished(url)
                    config.javascriptOnFinished?.let {
                        webView.evaluateJavascript(it, null)
                    }
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

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    request?.url?.toSKUri()?.let { skUri ->
                        try {
                           config.onRequest?.invoke(skUri)
                        } catch (ex: Exception) {
                            SKLog.e(
                                ex,
                                "Erreur dans l'invocation de shouldInterceptRequest depuis SKWebViewView"
                            )
                        }
                    }
                    return super.shouldInterceptRequest(view, request)
                }
            }

        } else {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                    url?.let { Uri.parse(url).toSKUri() }?.let { skUri ->
                        try {
                            if (config.shouldOverrideUrlLoading?.invoke(skUri) == true) {
                                return true
                            }
                        } catch (ex: Exception) {
                            SKLog.e(
                                ex,
                                "Erreur dans l'invocation de shouldOverrideUrlLoading depuis SKWebViewView"
                            )
                        }

                        try {
                            val skUri = Uri.parse(url).toSKUri()
                            if (skUri != null && activity.featureInitializer.onDeepLink?.invoke(
                                    skUri,
                                    true
                                ) == true
                            ) {
                                return true
                            }
                        } catch (ex: Exception) {
                            SKLog.e(
                                ex,
                                "Erreur dans l'invocation de onDeepLink depuis SKWebViewView"
                            )
                        }
                    }

                    oneRedirectionAskedForCurrentOpenUrl = true

                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    request?.url?.toSKUri()?.let { skUri ->
                        try {
                            config.onRequest?.invoke(skUri)
                        } catch (ex: Exception) {
                            SKLog.e(
                                ex,
                                "Erreur dans l'invocation de shouldInterceptRequest depuis SKWebViewView"
                            )
                        }
                    }
                    return super.shouldInterceptRequest(view, request)
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

    private var openingUrl: SKWebViewVC.Launch? = null

    private var oneRedirectionAskedForCurrentOpenUrl = false

    private fun SKWebViewVC.Launch.finished(finishedUrl: String?) {
        val escapedUrl =   if(this is SKWebViewVC.Launch.LoadData){
              "data:text/html,$data"
        }else{
            url?.replace(" ", "%20")
        }

        if (finishedUrl == escapedUrl || finishedUrl == "$escapedUrl/" || oneRedirectionAskedForCurrentOpenUrl) {
            openingUrl = null
            onFinished?.invoke(webView.title)
            javascriptOnFinished?.let {
                webView.evaluateJavascript(it, null)
            }

        }

    }

    private fun SKWebViewVC.Launch.error(requestedUri: Uri?) {
        when(this){
            is SKWebViewVC.Launch.LoadData -> {}
            is SKWebViewVC.Launch.OpenPostUrl -> launchError(requestedUri,this.url, this.onError)
            is SKWebViewVC.Launch.OpenUrl -> launchError(requestedUri,this.url, this.onError)
            is SKWebViewVC.Launch.OpenUrlWithHeader -> launchError(requestedUri,this.url, this.onError)
        }
    }

    private fun launchError(requestedUri: Uri?, url : String?,  onError: (() -> Unit)?){
        requestedUri?.toString()?.let { requestUrl ->
            if (url == requestUrl) {
                onError?.invoke()
                openingUrl = null
            }
        }
    }

    fun onLaunch(launch: SKWebViewVC.Launch?) {
        oneRedirectionAskedForCurrentOpenUrl = false
        openingUrl = launch
        if (launch != null) {
            if (launch.removeCookies) {
                CookieManager.getInstance().removeAllCookies {
                    launch.cookie?.let {
                        CookieManager.getInstance().setCookie(it.first,it.second)
                    }
                    launchNow(launch)
                }
            } else {
                launch.cookie?.let {
                    CookieManager.getInstance().setCookie(it.first,it.second)
                }
                launchNow(launch)
            }

        }
    }

    private fun launchNow(launch: SKWebViewVC.Launch) {
        when(launch){
            is SKWebViewVC.Launch.LoadData -> {
                if(launch.url != null){
                    binding.loadDataWithBaseURL(launch.url, launch.data,null,null, null)
                }else{
                    binding.loadData(launch.data,null,null)
                }
            }
            is SKWebViewVC.Launch.OpenPostUrl -> {
                val params = launch.post.map {
                    "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}"
                }
                    .joinToString(separator = "&")
                binding.postUrl(launch.url, params.toByteArray())
            }
            is SKWebViewVC.Launch.OpenUrl -> {
                binding.loadUrl(launch.url)
            }
            is SKWebViewVC.Launch.OpenUrlWithHeader -> {
                binding.loadUrl(launch.url,launch.headers)
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