package tech.skot.core.components.presented

import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import tech.skot.core.components.ComponentViewImpl
import tech.skot.core.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData

class AlertViewImpl(activity: SKActivity, fragment: Fragment?, private val proxy: AlertViewProxy) : ComponentViewImpl<Unit>(activity, fragment, Unit) {

    data class State(val state:AlertVC.Shown, val alert: AlertDialog)
    private var current:State? = null


    fun onState(state: AlertVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                AlertDialog.Builder(context)
                        .setTitle(state.title)
                        .setMessage(state.message)
                        .setOnDismissListener {
                            proxy.state = null
                        }
                        .setCancelable(false)
                        .apply {
                            setPositiveButton(state.mainButton.label, state.mainButton.action?.let { action ->
                                DialogInterface.OnClickListener { p0, p1 -> action() }
                            })

                            state.secondaryButton?.let { button ->
                                setNeutralButton(button.label, button.action?.let { action ->
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