package tech.skot.core.components.presented

import tech.skot.core.components.Component
import tech.skot.core.di.coreViewInjector

class Alert : Component<AlertVC>(){

    override val view = coreViewInjector.alert()

    fun show(
            title:String?,
            message:String?,
            mainButton: AlertVC.Button = AlertVC.Button(label = "Ok", action = null),
            secondaryButton: AlertVC.Button? = null
    ) {
        view.state = AlertVC.Shown(
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