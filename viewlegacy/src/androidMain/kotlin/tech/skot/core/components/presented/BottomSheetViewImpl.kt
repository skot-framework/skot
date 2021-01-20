package tech.skot.core.components.presented

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.legacy.ScreenViewProxy
import tech.skot.view.live.MutableSKLiveData

class BottomSheetViewProxy():ComponentViewProxy<Unit>(), BottomSheetView {

    private val stateLD = MutableSKLiveData<BottomSheetView.Shown?>(null)

    override var state: BottomSheetView.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: Unit) =
            BottomSheetViewImpl(activity, fragment, this).apply {
                stateLD.observe {
                    onState(it)
            }
        }




}

class BottomSheetViewImpl(activity: SKActivity, fragment: SKFragment?, private val proxy: BottomSheetViewProxy) : ComponentViewImpl<Unit>(activity, fragment, Unit) {

    data class State(val state:BottomSheetView.Shown, val bottomSheet: BottomSheetDialog)
    private var current:State? = null

    fun onState(state:BottomSheetView.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                BottomSheetDialog(context, 0).apply {
                    setOnDismissListener {
                        proxy.onDismiss()
                    }
                    setContentView((state.screen as ScreenViewProxy<*>).bindTo(activity, fragment, layoutInflater))
                    show()
                    current = State(state, this)
                }
            }
            else {
                current?.bottomSheet?.dismiss()
                current = null
            }

        }

    }
}