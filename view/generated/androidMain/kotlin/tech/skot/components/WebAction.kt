package tech.skot.components

import tech.skot.view.Action

sealed class WebAction : Action() {
    class OpenUrl(
            val url: String,
            val onFinished: Function0<Unit>?,
            val onError: Function0<Unit>?
    ) : WebAction()
}
