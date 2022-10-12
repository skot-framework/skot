package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

/**
 *  # SKAlert
 *  ## An alert dialog helper component
 *
 */
class SKAlert : SKComponent<SKAlertVC>() {

    override val view = coreViewInjector.alert()

    /**
     * display an alert dialog
     *
     * @param title title of the alert dialog
     */
    fun show(
        title: String? = null,
        message: String?,
        cancelable: Boolean = false,
        withInput: Boolean = false,
        mainButton: SKAlertVC.Button = SKAlertVC.Button(label = "Ok", action = null),
        secondaryButton: SKAlertVC.Button? = null,
        neutralButton: SKAlertVC.Button? = null
    ) {
        view.state = SKAlertVC.Shown(
            title = title,
            message = message,
            cancelable = cancelable,
            withInput = withInput,
            mainButton = mainButton,
            secondaryButton = secondaryButton,
            neutralButton = neutralButton
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