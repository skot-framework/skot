package tech.skot.core.components

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import tech.skot.core.di.get

class SnackBar : Component<SnackBarView>() {

    override val view = get<SnackBarView>()

    data class Action(val label: String, val action: () -> Unit)

    private var disappearJob: Job? = null
    fun show(message: String, action: SnackBar.Action? = null) {
        view.state = SnackBarView.Shown(
                message = message,
                action = action?.let {
                    SnackBarView.Action(
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