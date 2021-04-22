package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.*


class SKBottomSheetView(activity: SKActivity, fragment: Fragment?, private val proxy: SKBottomSheetViewProxy) : SKComponentView<Unit>(activity, fragment, Unit) {

    data class State(val state: SKBottomSheetVC.Shown, val bottomSheet: SKBottomSheetDialogFragment)

    private var current: State? = null

    fun onState(state: SKBottomSheetVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                (state.screen as SKScreenViewProxy<*>).createDialogFragment().apply {
                    show(this@SKBottomSheetView.fragmentManager, "Bottom")
                    setOnDismissListener {
                        proxy.state = null
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