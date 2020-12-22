package tech.skot.core.components.presented

import tech.skot.core.components.Component
import tech.skot.core.di.get

class Alert : Component<AlertView>(){

    override val view = get<AlertView>()

    fun show(title:String?, message:String?) {
        view.state = AlertView.Shown(
                title = title,
                message = message,
                buttons = listOf(
                        AlertView.Button(label = "Ok", action = { dismiss() })
                ),
                onDismissRequest = { dismiss() }
        )
    }

    fun dismiss() {
        view.state = null
    }
}