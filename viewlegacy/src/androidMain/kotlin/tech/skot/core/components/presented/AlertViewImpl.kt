package tech.skot.core.components.presented

import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData

class AlertViewProxy() : ComponentViewProxy<Unit>(), AlertView {

    private val stateLD = MutableSKLiveData<AlertView.Shown?>(null)

    override var state: AlertView.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: Unit) =
            AlertViewImpl(activity, fragment, this).apply {
                stateLD.observe {
                    onState(it)
                }
            }


}

class AlertViewImpl(activity: SKActivity, fragment: SKFragment?, private val proxy: AlertViewProxy) : ComponentViewImpl<Unit>(activity, fragment, Unit) {

    data class State(val state:AlertView.Shown, val alert: AlertDialog)
    private var current:State? = null


    fun onState(state: AlertView.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                AlertDialog.Builder(context)
                        .setTitle(state.title)
                        .setMessage(state.message)
                        .setOnDismissListener {
                            proxy.onDismiss()
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