package tech.skot.core.components.presented

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.skot.core.SKLog
import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.Color
import tech.skot.core.view.Icon

class SKSnackBar : SKComponent<SKSnackBarVC>() {

    override val view = coreViewInjector.snackBar()

    data class Action(val label: String, val action: () -> Unit)

    private var disappearJob: Job? = null
    fun show(
        message: String,
        action: Action? = null,
        onTop: Boolean = false,
        duration: Long = 3000,
        leftIcon:Icon? = null,
        rightIcon:Icon? = null,
        backgroundColor: Color? = null
    ) {
        view.state = SKSnackBarVC.Shown(
            message = message,
            action = action?.let {
                SKSnackBarVC.Action(
                    label = it.label,
                    action = it.action
                )
            },
            onTop = onTop,
            leftIcon = leftIcon,
            rightIcon = rightIcon,
            backgroundColor = backgroundColor
        )
        disappearJob?.cancel()
        disappearJob = launch {
            delay(duration)
            view.state = null
        }
    }

}