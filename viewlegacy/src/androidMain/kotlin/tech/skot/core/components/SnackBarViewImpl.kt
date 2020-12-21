package tech.skot.core.components

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.view.live.MutableSKLiveData

class SnackBarViewProxy() : ComponentViewProxy<View>(), SnackBarView {

    private val stateLD = MutableSKLiveData<SnackBarView.Shown?>(null)

    override var state: SnackBarView.Shown? = null
        get() = stateLD.value
        set(newVal) {
            field = newVal
            stateLD.postValue(newVal)
        }

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: View) =
            SnackBarViewImpl(activity, fragment, binding).apply {
                stateLD.observe {
                    onState(it)
                }
            }


}

class SnackBarViewImpl(activity: SKActivity, fragment: SKFragment?, private val rootView: View) : ComponentViewImpl<View>(activity, fragment, rootView) {

    private var currentSnack: Snackbar? = null
    private var currentState: SnackBarView.Shown? = null

    fun onState(state: SnackBarView.Shown?) {

        if (state != currentState) {
            if (state != null) {

                Snackbar.make(rootView, state.message, Snackbar.LENGTH_INDEFINITE)
                        .apply {
                            state.action?.let { (label, action) ->
                                setAction(label, View.OnClickListener {
                                    action()
                                })

                            }
                            view.apply {
                                (layoutParams as? FrameLayout.LayoutParams)?.let {
                                    it.gravity = Gravity.TOP
                                    it.topMargin = activity.window?.decorView?.rootWindowInsets?.systemWindowInsetTop
                                            ?: 0

                                    layoutParams = it
                                }
                            }
                            currentSnack = this
                            show()
                        }
            } else {
                currentSnack?.dismiss()
            }

        }


    }
}