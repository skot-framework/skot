package tech.skot.core.components.presented

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKActivity

class SKAlertView(override val proxy: SKAlertViewProxy, activity: SKActivity, fragment: Fragment?) : SKComponentView<Unit>(proxy, activity, fragment, Unit) {

    data class State(val state:SKAlertVC.Shown, val alert: AlertDialog)
    private var current:State? = null


    fun onState(state: SKAlertVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                AlertDialog.Builder(context)
                        .setTitle(state.title)
                        .setMessage(state.message)
                        .setOnDismissListener {
                            proxy.state = null
                        }
                        .setCancelable(state.cancelable)
                        .apply {
                            setPositiveButton(state.mainButton.label, state.mainButton.action?.let { action ->
                                DialogInterface.OnClickListener { p0, p1 -> action() }
                            })

                            state.secondaryButton?.let { button ->
                                setNegativeButton(button.label, button.action?.let { action ->
                                    DialogInterface.OnClickListener { p0, p1 -> action() }
                                })
                            }
                        }
                        .create()
                        .let {
                            current = State(state, it)
                            it.show()
                        }
            } else {
                current?.alert?.dismiss()
                current = null
            }

        }

    }
}