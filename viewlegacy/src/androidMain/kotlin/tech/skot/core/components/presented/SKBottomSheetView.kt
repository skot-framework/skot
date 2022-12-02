package tech.skot.core.components.presented

import androidx.fragment.app.Fragment
import tech.skot.core.components.*


class SKBottomSheetView(
    override val proxy: SKBottomSheetViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
) : SKComponentView<Unit>(proxy, activity, fragment, Unit) {

    data class State(val state: SKBottomSheetVC.Shown, val bottomSheet: SKBottomSheetDialogFragment)

    private var current: State? = null

    fun onState(state: SKBottomSheetVC.Shown?) {

        if (state != current?.state) {
            current?.bottomSheet?.dismiss()
            if (state != null) {
                (state.screen as SKScreenViewProxy<*>).createBottomSheetFragment(
                    expanded = state.expanded,
                    skipCollapsed = state.skipCollapsed,
                    fullHeight = state.fullHeight,
                    resizeOnKeyboard = state.resizeOnKeyboard
                ).apply {
                    show(this@SKBottomSheetView.fragmentManager, "SkBottomSheet")

                    setOnDismissListener {
                        if (proxy.state == state) {
                            proxy.state = null
                        }
                        state.onDismiss?.invoke()
                    }
                    current = State(state, this)
                }

            } else {

                current = null
            }

        }

    }
}