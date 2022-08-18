package tech.skot.core.components.presented

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.skot.core.components.SKComponent
import tech.skot.core.di.coreViewInjector
import tech.skot.core.view.*

class SKSnackBar : SKComponent<SKSnackBarVC>() {

    override val view = coreViewInjector.snackBar()

    data class Action(val label: String, val action: () -> Unit)

    private var disappearJob: Job? = null

    fun show(
        message: String,
        action: Action? = null,
        position: SKSnackBarVC.Position = SKSnackBarVC.Position.TopWithInsetMargin,
        duration: Long = 3000,
        leftIcon: Icon? = null,
        rightIcon: Icon? = null,
        background: Resource? = null,
        textColor: Color? = null,
        infiniteLines: Boolean = false,
        centerText: Boolean = false

    ) {
        show(
            skSpannedString { append(message) },
            action,
            position,
            duration,
            leftIcon,
            rightIcon,
            background,
            textColor,
            infiniteLines,
            centerText
        )
    }

    fun show(
        message: SKSpannedString,
        action: Action? = null,
        position: SKSnackBarVC.Position = SKSnackBarVC.Position.TopWithInsetMargin,
        duration: Long = 3000,
        leftIcon: Icon? = null,
        rightIcon: Icon? = null,
        background: Resource? = null,
        textColor: Color? = null,
        infiniteLines: Boolean = false,
        centerText: Boolean = false

    ) {
        view.state = SKSnackBarVC.Shown(
            message = message,
            action = action?.let {
                SKSnackBarVC.Action(
                    label = it.label,
                    action = it.action
                )
            },
            position = position,
            leftIcon = leftIcon,
            rightIcon = rightIcon,
            background = background,
            textColor = textColor,
            infiniteLines = infiniteLines,
            centerText = centerText
        )
        disappearJob?.cancel()
        disappearJob = launch {
            delay(duration)
            view.state = null
        }
    }

}