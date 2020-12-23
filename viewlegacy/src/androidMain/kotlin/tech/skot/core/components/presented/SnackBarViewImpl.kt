package tech.skot.core.components.presented

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData

class SnackBarViewProxy() : ComponentViewProxy<View>(), SnackBarView {

    private val stateLD = MutableSKLiveData<SnackBarView.Shown?>(null)

    override var state: SnackBarView.Shown? by stateLD

    override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: View) =
            SnackBarViewImpl(activity, fragment, binding, this).apply {
                stateLD.observe {
                    onState(it)
                }
            }


}

class SnackBarViewImpl(activity: SKActivity, fragment: SKFragment?, private val rootView: View, private val proxy:SnackBarViewProxy) : ComponentViewImpl<View>(activity, fragment, rootView) {

    data class State(val state:SnackBarView.Shown, val snack:Snackbar)
    private var current:State? = null

    fun onState(state: SnackBarView.Shown?) {

        SKLog.d("onState $state    current $current")
        if (state != current?.state) {
            if (state != null) {

                Snackbar.make(rootView, state.message, Snackbar.LENGTH_INDEFINITE)
                        .apply {
                            state.action?.let { (label, action) ->
                                setAction(label, View.OnClickListener {
                                    action()
                                    proxy.onDismiss()
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
                            current = State(state, this)
                            show()
                        }
            } else {
                SKLog.d("State == null will dismiss ")
                current?.snack?.dismiss()
                current = null
            }

        }


    }
}