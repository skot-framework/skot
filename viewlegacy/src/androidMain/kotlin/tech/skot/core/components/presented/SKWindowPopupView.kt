package tech.skot.core.components.presented

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKComponentViewProxy

class SKWindowPopupView(
    override val proxy: SKWindowPopupViewProxy,
    activity: SKActivity,
    fragment: Fragment?
) : SKComponentView<Unit>(proxy, activity, fragment, Unit) {

    data class State(val state: SKWindowPopupVC.Shown, val popup: PopupWindow)

    private var current: State? = null

    var anchor: View? = null
    var widthSize: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    var heightSize: Int = ViewGroup.LayoutParams.WRAP_CONTENT


    fun onState(state: SKWindowPopupVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                current?.popup?.dismiss()
                current = null
                val view =
                    (state.component as SKComponentViewProxy<*>).inflateAndBind(activity, fragment)

                val trueView = when (view) {
                    is View -> view
                    is ViewBinding -> view.root
                    else -> throw UnsupportedOperationException("Use view or viewBinding component")
                }

                val popupWindow = PopupWindow(
                    trueView,
                    widthSize,
                    heightSize
                )

                when (val comportement = state.behavior) {
                    is SKWindowPopupVC.Cancelable -> {
                        popupWindow.isOutsideTouchable = true
                        popupWindow.isFocusable = true
                        popupWindow.setOnDismissListener {
                            comportement.onDismiss?.invoke()
                        }
                    }
                    SKWindowPopupVC.NotCancelable -> {
                        popupWindow.isOutsideTouchable = false
                        popupWindow.isFocusable = false
                    }
                }
                if (fragment?.isAdded != false && !activity.isDestroyed) {
                    popupWindow.showAsDropDown(anchor)
                    current = State(state, popupWindow)
                }


            } else {
                current?.popup?.dismiss()
                current = null
            }
        }
    }
}

