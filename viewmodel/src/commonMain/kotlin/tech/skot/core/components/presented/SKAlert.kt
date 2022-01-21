package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

class SKAlert : SKComponent<SKAlertVC>() {

    override val view = coreViewInjector.alert()

    fun show(
        title: String? = null,
        message: String?,
        cancelable: Boolean = false,
        withInput: Boolean = false,
        mainButton: SKAlertVC.Button = SKAlertVC.Button(label = "Ok", action = null),
        secondaryButton: SKAlertVC.Button? = null
    ) {
        view.state = SKAlertVC.Shown(
            title = title,
            message = message,
            cancelable = cancelable,
            withInput = withInput,
            mainButton = mainButton,
            secondaryButton = secondaryButton
        )
    }

    var inputText: String?
        get() = view.inputText
        set(value) {
            view.inputText = value
        }

    fun dismiss() {
        view.state = null
    }
}