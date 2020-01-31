package tech.skot.components


class Web(vararg redirect: WebView.RedirectParam) : Component<WebView>() {
    override val view: WebView = coreViewInjector.web(
            redirect = redirect.asList()
    )
}
