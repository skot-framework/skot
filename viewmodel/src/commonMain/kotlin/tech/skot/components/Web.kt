package tech.skot.components


class Web(vararg redirect: WebView.RedirectParam, userAgent:String? = null, onCantBack:(()->Unit)? = null) : Component<WebView>() {
    override val view: WebView = coreViewInjector.web(
            redirect = redirect.asList(),
            userAgent = userAgent,
            onCantBack = onCantBack
    )
}
