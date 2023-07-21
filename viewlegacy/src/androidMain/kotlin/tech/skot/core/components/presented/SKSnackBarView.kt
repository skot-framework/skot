package tech.skot.core.components.presented

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.setBackground
import tech.skot.core.toColor
import tech.skot.core.view.toCharSequence
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
    var container : View? = null

    init {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
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
                Snackbar.make(container?:baseView,  state.message.toCharSequence(context), Snackbar.LENGTH_INDEFINITE)
                    .apply {
                        isGestureInsetBottomIgnored = state.isGestureInsetBottomIgnored

                        if(state.slideAnimation) {
                            this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                        }
                        if (state.leftIcon != null || state.rightIcon != null || state.infiniteLines || state.centerText) {
                            try {

                                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                                    .apply {
                                        if (state.message.any { it.format.onTap != null }) {
                                            movementMethod = LinkMovementMethod.getInstance()
                                            highlightColor = Color.TRANSPARENT
                                        }

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
                                            compoundDrawablePadding = resources.getDimensionPixelOffset(
                                                R.dimen.sk_snackbar_icon_padding
                                            )
                                        }
                                        if (state.centerText) {
                                            gravity = Gravity.CENTER_HORIZONTAL
                                        }
                                    }


                            } catch (ex: Exception) {
                                SKLog.e(ex, "Problem on settings images to Snack")
                            }
                        }

                        state.background?.let { bg ->
                            view.setBackground(bg)
                        }

                        state.textColor?.let {
                            this.setTextColor(it.toColor(context))
                        }


                        state.action?.let { (label, action) ->
                            setAction(label, View.OnClickListener {
                                action()
                                proxy.state = null
                            })

                        }
                        anchor?.let { setAnchorView(it) }
                        when (val position = state.position) {
                            SKSnackBarVC.Position.Bottom -> {
                                view.apply {
                                    (layoutParams as? MarginLayoutParams)?.let {
                                        it.bottomMargin = 0
                                        layoutParams = it
                                    }
                                }
                            }
                            is SKSnackBarVC.Position.BottomWithCustomMargin -> {
                                view.apply {
                                    (layoutParams as? MarginLayoutParams)?.let {
                                            it.bottomMargin = position.margin.toPx.toInt()
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
                                                baseView.rootWindowInsets?.systemWindowInsetTop
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
                                                view.paddingTop + (baseView.rootWindowInsets?.systemWindowInsetTop
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