package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKDialogFragment
import tech.skot.core.components.SKScreenViewProxy


class SKDialogView(
    override val proxy: SKDialogViewProxy,
    activity: SKActivity,
    fragment: Fragment?
) : SKComponentView<Unit>(proxy, activity, fragment, Unit) {

    data class State(val state: SKDialogVC.Shown, val dialog: SKDialogFragment)

    private var current: State? = null

    fun onState(state: SKDialogVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                val oldStatusBarColor = activity.window.statusBarColor
                (state.screen as SKScreenViewProxy<*>).createDialogFragment().apply {
                    isCancelable = state.cancelable
                    show(this@SKDialogView.fragmentManager, "SKDialog")
                    setOnDismissListener {
                        this@SKDialogView.activity.window.statusBarColor = oldStatusBarColor
                        proxy.state = null
                        state.onDismiss?.invoke()
                    }
                    current = State(state, this)
                }

            } else {
                current?.dialog?.dismiss()
                current = null
            }

        }

    }
}