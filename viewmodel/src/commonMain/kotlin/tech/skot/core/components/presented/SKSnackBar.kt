package tech.skot.core.components.presented

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.skot.core.components.Component
import tech.skot.core.di.coreViewInjector

class SKSnackBar : Component<SKSnackBarVC>() {

    override val view = coreViewInjector.snackBar()

    data class Action(val label: String, val action: () -> Unit)

    private var disappearJob: Job? = null
    fun show(message: String, action: Action? = null) {
        view.state = SKSnackBarVC.Shown(
                message = message,
                action = action?.let {
                    SKSnackBarVC.Action(
                            label = it.label,
                            action = it.action
                    )
                },
        )
        disappearJob?.cancel()
        disappearJob = launch {
            delay(3000)
            view.state = null
        }
    }
}