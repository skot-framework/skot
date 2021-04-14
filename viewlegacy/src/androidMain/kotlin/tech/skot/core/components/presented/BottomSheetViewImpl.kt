package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.*


class BottomSheetViewImpl(activity: SKActivity, fragment: Fragment?, private val proxy: BottomSheetViewProxy) : ComponentView<Unit>(activity, fragment, Unit) {

    data class State(val state: BottomSheetVC.Shown, val bottomSheet: SKBottomSheetDialogFragment)

    private var current: State? = null

    fun onState(state: BottomSheetVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                (state.screen as ScreenViewProxy<*>).createDialogFragment().apply {
                    show(this@BottomSheetViewImpl.fragmentManager, "Bottom")
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