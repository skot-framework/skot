package tech.skot.core.components.presented

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
                        if (state.leftIcon != null || state.rightIcon != null || state.infiniteLines) {
                            try {

                                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                                    .apply {
                                        if (state.infiniteLines) {
                                            this.isSingleLine = false
                                        }
                                        if (state.leftIcon != null || state.rightIcon != null) {
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
                                    }


                            } catch (ex: Exception) {
                                SKLog.e(ex, "Problem on settings images to Snack")
                            }
                        }

                        state.background?.let { bg ->
                            view.setBackgroundResource(bg.res)
                        }

                        state.textColor?.res?.let {
                            this.setTextColor(ContextCompat.getColor(context, it))
                        }


                        state.action?.let { (label, action) ->
                            setAction(label, View.OnClickListener {
                                action()
                                proxy.state = null
                            })

                        }
                        when (val position = state.position) {
                            SKSnackBarVC.Position.Bottom -> {}
                            is SKSnackBarVC.Position.BottomWithCustomMargin -> {
                                view.apply {
                                    (layoutParams as? FrameLayout.LayoutParams)?.let {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            it.bottomMargin = position.margin.toPx.toInt()
                                        }
                                        layoutParams = it
                                    }
                                }
                            }
                            SKSnackBarVC.Position.TopWithInsetMargin -> {
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
                            SKSnackBarVC.Position.TopWithInsetPadding -> {
                                view.apply {
                                    (layoutParams as? FrameLayout.LayoutParams)?.let {
                                        it.gravity = Gravity.TOP
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            view.setPadding(
                                                view.paddingLeft,
                                                view.paddingTop + (activity.window?.decorView?.rootWindowInsets?.systemWindowInsetTop
                                                    ?: 0),
                                                view.paddingRight,
                                                view.paddingBottom
                                            )
                                        }
                                        layoutParams = it
                                    }
                                }
                            }
                            is SKSnackBarVC.Position.TopWithCustomMargin -> {
                                view.apply {
                                    (layoutParams as? FrameLayout.LayoutParams)?.let {
                                        it.gravity = Gravity.TOP
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            it.topMargin = position.margin.toPx.toInt()
                                        }
                                        layoutParams = it
                                    }
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

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )


}