package tech.skot.core.components.presented

import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

class SKAlert : SKComponent<SKAlertVC>(){

    override val view = coreViewInjector.alert()

    fun show(
            title:String?,
            message:String?,
            mainButton: SKAlertVC.Button = SKAlertVC.Button(label = "Ok", action = null),
            secondaryButton: SKAlertVC.Button? = null
    ) {
        view.state = SKAlertVC.Shown(
                title = title,
                message = message,
                mainButton = mainButton,
                secondaryButton = secondaryButton
        )
    }

    fun dismiss() {
        view.state = null
    }
}