package tech.skot.core.components.presented

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.viewlegacy.R

class SKSnackBarView(
    override val proxy: SKSnackBarViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val rootView: View
) : SKComponentView<View>(proxy, activity, fragment, rootView) {

    data class State(val state: SKSnackBarVC.Shown, val snack: Snackbar)

    private var current: State? = null

    var anchor: View? = null

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                current?.snack?.dismiss()
                current = null
                proxy.state = null
            }
        }
        )
    }


    fun onState(state: SKSnackBarVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {

                Snackbar.make(activity.window.decorView, state.message, Snackbar.LENGTH_INDEFINITE)
                    .apply {

                        if (state.leftIcon != null || state.rightIcon != null) {
                            try {
                                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                                    .apply {
                                        setCompoundDrawablesWithIntrinsicBounds(
                                            state.leftIcon?.res ?: 0,
                                            0,
                                            state.rightIcon?.res ?: 0,
                                            0
                                        )
                                        setCompoundDrawablePadding(
                                            getResources().getDimensionPixelOffset(
                                                R.dimen.sk_snackbar_icon_padding
                                            )
                                        )
                                    }


                            } catch (ex: Exception) {
                                SKLog.e(ex, "Problem on settings images to Snack")
                            }
                        }

                        state.backgroundColor?.res?.let { resColor ->
                            view.setBackgroundResource(resColor)
                        }

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
                                        it.topMargin =
                                            activity.window?.decorView?.rootWindowInsets?.systemWindowInsetTop
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
                current?.snack?.dismiss()
                current = null
            }

        }


    }
}