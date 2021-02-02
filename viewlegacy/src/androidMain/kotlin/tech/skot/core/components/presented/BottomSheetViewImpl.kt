package tech.skot.core.components.presented

import com.google.android.material.bottomsheet.BottomSheetDialog
import tech.skot.core.components.ComponentViewImpl
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.core.components.ScreenViewProxy


class BottomSheetViewImpl(activity: SKActivity, fragment: SKFragment?, private val proxy: BottomSheetViewProxy) : ComponentViewImpl<Unit>(activity, fragment, Unit) {

    data class State(val state: BottomSheetVC.Shown, val bottomSheet: BottomSheetDialog)

    private var current: State? = null

    fun onState(state: BottomSheetVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                BottomSheetDialog(context, 0).apply {
                    setOnDismissListener {
                        proxy.state = null
                    }
                    setContentView((state.screen as ScreenViewProxy<*>).bindTo(activity, fragment, layoutInflater))
                    show()
                    current = State(state, this)
                }
            } else {
                current?.bottomSheet?.dismiss()
                current = null
            }

        }

    }
}