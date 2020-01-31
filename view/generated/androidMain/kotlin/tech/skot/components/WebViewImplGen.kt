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
            onError: Function0<Unit>?
    )

    override fun linkTo(lifecycleOwner: LifecycleOwner) {
        super.linkTo(lifecycleOwner)
        onRedirect(redirect)
    }

    final override fun openUrl(
            url: String,
            onFinished: Function0<Unit>?,
            onError: Function0<Unit>?
    ) {
        messages.post(WebAction.OpenUrl(url, onFinished, onError))
    }

    override fun treatAction(action: Action) {
        when (action) {
            is WebAction.OpenUrl -> openUrlNow(action.url, action.onFinished, action.onError)
            else -> super.treatAction(action)
        }
    }
}