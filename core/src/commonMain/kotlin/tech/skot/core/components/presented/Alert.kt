package tech.skot.core.components.presented

import tech.skot.core.components.Component
import tech.skot.core.di.coreViewInjector
import tech.skot.core.di.get

class Alert : Component<AlertView>(){

    override val view = coreViewInjector.alert()

    fun show(
            title:String?,
            message:String?,
            mainButton: AlertView.Button = AlertView.Button(label = "Ok", action = null),
            secondaryButton: AlertView.Button? = null
    ) {
        view.state = AlertView.Shown(
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