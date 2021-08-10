package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.*


class SKBottomSheetView(override val proxy: SKBottomSheetViewProxy, activity: SKActivity, fragment: Fragment?) : SKComponentView<Unit>(proxy, activity, fragment, Unit) {

    data class State(val state: SKBottomSheetVC.Shown, val bottomSheet: SKBottomSheetDialogFragment)

    private var current: State? = null

    fun onState(state: SKBottomSheetVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                (state.screen as SKScreenViewProxy<*>).createDialogFragment().apply {
                    show(this@SKBottomSheetView.fragmentManager, "Bottom")
                    setOnDismissListener {
                        proxy.state = null
                        state.onDismiss?.invoke()
                    }
                    current = State(state, this)
                }

            } else {
                current?.bottomSheet?.dismiss()
                current = null
            }

        }

    }
}