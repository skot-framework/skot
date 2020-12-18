package tech.skot.core.components

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.SKLog
import tech.skot.view.live.MutableSKLiveData

class AlertViewProxy():ComponentViewProxy<AlertViewImpl>(),AlertView {

    private val stateLD = MutableSKLiveData<AlertView.Shown?>(null)

    override var state: AlertView.Shown? = null
        get() = stateLD.value
        set(newVal) {
            field = newVal
            stateLD.postValue(newVal)
        }

    override fun linkTo(impl: AlertViewImpl, lifeCycleOwner: LifecycleOwner) {
        stateLD.observe(lifeCycleOwner) {
            impl.onState(it)
        }
    }


}

class AlertViewImpl(activity: SKActivity, fragment: SKFragment?) : ComponentViewImpl<Unit>(activity, fragment, Unit) {

    private var currentAlert:AlertDialog? = null
    private var currentState:AlertView.Shown? = null

    fun onState(state:AlertView.Shown?) {

        if (state != currentState) {
            if (state != null) {
                AlertDialog.Builder(context)
                        .setTitle(state.title)
                        .setMessage(state.message)
                        .setCancelable(false)
                        .create()
                        .let {
                            currentAlert = it
                            currentState = state
                            it.show()
                        }
            }
            else {
                currentAlert?.dismiss()
            }

        }

    }
}