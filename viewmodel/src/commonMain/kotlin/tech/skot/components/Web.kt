package tech.skot.components


class Web(vararg redirect: WebView.RedirectParam, private val userAgent:String? = null) : Component<WebView>() {
    override val view: WebView = coreViewInjector.web(
            redirect = redirect.asList(),
            userAgent = userAgent
    )
}
