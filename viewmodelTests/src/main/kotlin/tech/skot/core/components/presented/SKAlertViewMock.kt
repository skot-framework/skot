package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock


class SKAlertViewMock : SKComponentViewMock(), SKAlertVC {
    override var state: SKAlertVC.Shown? = null
}

fun SKAlertVC.displayedWith(title: String? = null, message: String? = null): Boolean {
    return state != null && (title == null || title == state?.title) && (message == null || message == state?.message)
}

fun SKAlertVC.userTapMainButton() {
    state?.mainButton?.action?.invoke() ?: throw Exception("Pas de main bouton")
}

fun SKAlertVC.userTapSecondaryButton() {
    state?.secondaryButton?.action?.invoke() ?: throw Exception("Pas de second bouton")
}