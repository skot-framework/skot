package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import tech.skot.view.Action

abstract class WebViewImplGen(
        override val redirect: List<WebView.RedirectParam>
) : ComponentViewImpl<AppCompatActivity, Fragment, android.webkit.WebView>(), WebView {
    abstract fun onRedirect(redirect: List<WebView.RedirectParam>)

    abstract fun openUrlNow(
            url: String,
            onFinished: Function0<Unit>?,
            javascriptOnFinished: String?,
            onError: Function0<Unit>?,
            post: Map<String, String>?
    )

    abstract fun backNow()

    override fun linkTo(lifecycleOwner: LifecycleOwner) {
        super.linkTo(lifecycleOwner)
        onRedirect(redirect)
    }

    final override fun openUrl(
            url: String,
            onFinished: Function0<Unit>?,
            javascriptOnFinished: String?,
            onError: Function0<Unit>?,
            post:Map<String,String>?
    ) {
        messages.post(WebAction.OpenUrl(url, onFinished, javascriptOnFinished, onError, post))
    }

    final override fun back() {
        messages.post(WebAction.Back)
    }

    override fun treatAction(action: Action) {
        when (action) {
            is WebAction.OpenUrl -> openUrlNow(action.url, action.onFinished, action.javascriptOnFinished, action.onError, action.post)
            is WebAction.Back -> backNow()
            else -> super.treatAction(action)
        }
    }
}