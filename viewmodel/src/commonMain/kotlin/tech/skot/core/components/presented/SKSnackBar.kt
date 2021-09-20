package tech.skot.core.components.presented

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.skot.core.SKLog
import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector

class SKSnackBar : SKComponent<SKSnackBarVC>() {

    override val view = coreViewInjector.snackBar()

    data class Action(val label: String, val action: () -> Unit)

    private var disappearJob: Job? = null
    fun show(message: String, action: Action? = null, onTop: Boolean = false, duration:Long = 3000) {
        view.state = SKSnackBarVC.Shown(
            message = message,
            action = action?.let {
                SKSnackBarVC.Action(
                    label = it.label,
                    action = it.action
                )
            },
            onTop = onTop
        )
        disappearJob?.cancel()
        disappearJob = launch {
            delay(duration)
            view.state = null
        }
    }

}