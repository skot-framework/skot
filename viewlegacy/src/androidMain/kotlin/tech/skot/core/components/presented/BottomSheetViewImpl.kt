package tech.skot.core.components

import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.SKLog
import tech.skot.core.components.presented.BottomSheetView
import tech.skot.view.legacy.ScreenViewProxy
import tech.skot.view.live.MutableSKLiveData

class BottomSheetViewProxy():ComponentViewProxy<Unit>(), BottomSheetView {

    private val stateLD = MutableSKLiveData<BottomSheetView.Shown?>(null)

    override var state: BottomSheetView.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: Unit) =
            BottomSheetViewImpl(activity, fragment).apply {
                stateLD.observe {
                    onState(it)
            }
        }




}

class BottomSheetViewImpl(activity: SKActivity, fragment: SKFragment?) : ComponentViewImpl<Unit>(activity, fragment, Unit) {

    private var currentBottomSheet:BottomSheetDialog? = null
    private var currentState:BottomSheetView.Shown? = null



    fun onState(state:BottomSheetView.Shown?) {

        if (state != currentState) {
            if (state != null) {
                BottomSheetDialog(context, 0).apply {
                    setOnDismissListener {
                        SKLog.d("---------- onDismiss !!!")
                    }
                    setContentView((state.screen as ScreenViewProxy<*>).bindTo(activity, fragment, layoutInflater))
                    show()
                    currentBottomSheet = this
                    currentState = state
                }
            }
            else {
                currentBottomSheet?.dismiss()
            }

        }

    }
}