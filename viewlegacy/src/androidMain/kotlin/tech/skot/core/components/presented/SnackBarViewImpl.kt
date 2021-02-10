package tech.skot.core.components.presented

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import tech.skot.core.components.ComponentViewImpl
import tech.skot.core.components.ComponentViewProxy
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.view.live.MutableSKLiveData

class SnackBarViewImpl(activity: SKActivity, fragment: Fragment?, private val rootView: View, private val proxy:SnackBarViewProxy) : ComponentViewImpl<View>(activity, fragment, rootView) {

    data class State(val state:SnackBarVC.Shown, val snack:Snackbar)
    private var current:State? = null

    fun onState(state: SnackBarVC.Shown?) {

        SKLog.d("onState $state    current $current")
        if (state != current?.state) {
            if (state != null) {

                Snackbar.make(rootView, state.message, Snackbar.LENGTH_INDEFINITE)
                        .apply {
                            state.action?.let { (label, action) ->
                                setAction(label, View.OnClickListener {
                                    action()
                                    proxy.state = null
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