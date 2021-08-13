package tech.skot.core.components.presented

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import tech.skot.core.SKLog
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKActivity

class SKSnackBarView(override val proxy: SKSnackBarViewProxy, activity: SKActivity, fragment: Fragment?, private val rootView: View) : SKComponentView<View>(proxy, activity, fragment, rootView) {

    data class State(val state: SKSnackBarVC.Shown, val snack: Snackbar)

    private var current: State? = null

    var anchor:View? = null

    fun onState(state: SKSnackBarVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {

                Snackbar.make(activity.window.decorView, state.message, Snackbar.LENGTH_INDEFINITE)
                        .apply {
                            state.action?.let { (label, action) ->
                                setAction(label, View.OnClickListener {
                                    action()
                                    proxy.state = null
                                })

                            }
                            if (state.onTop) {
                                view.apply {
                                    (layoutParams as? FrameLayout.LayoutParams)?.let {
                                        it.gravity = Gravity.TOP
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            it.topMargin = activity.window?.decorView?.rootWindowInsets?.systemWindowInsetTop
                                                    ?: 0
                                        }

                                        layoutParams = it
                                    }
                                }
                            }
                            current = State(state, this)
                            anchor?.let { setAnchorView(it) }
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