package tech.skot.core.components

import android.view.View
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.core.toColor
import tech.skot.core.view.Color
import tech.skot.view.extensions.systemBars
import tech.skot.view.extensions.updatePadding


abstract class SKScreenView<B : ViewBinding>(
    override val proxy: SKScreenViewProxy<B>,
    activity: SKActivity,
    fragment: Fragment?,
    binding: B
) : SKComponentView<B>(proxy, activity, fragment, binding) {
    val view: View = binding.root


    private var onBackPressed: (() -> Unit)? = null
    fun setOnBackPressed(onBackPressed: (() -> Unit)?) {
        this.onBackPressed = onBackPressed
    }


    protected val originalPaddingTop = view.paddingTop


    @CallSuper
    open fun onResume() {
        if (fragment !is DialogFragment) {
            if (secured) {
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
            }
            else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }

            activity.setFullScreen(
                fullScreen,
                lightStatusBar,
                onWindowInset ?: (if (withWindowsInsetsPaddingTop) {
                    {
                        view.updatePadding(top = originalPaddingTop + it.systemBars().top)
                    }
                } else null)
            )
        }
        onStatusBarColor(proxy.statusBarColor)
        proxy.onResume()

    }

    @CallSuper
    open fun onPause() {
        proxy.onPause()
    }


    open val fullScreen: Boolean = false
    open val lightStatusBar: Boolean? = null
    open val secured:Boolean = false

    fun onStatusBarColor(color: Color?) {
        activity.requestedStatusBarColor = color?.toColor(context)
        activity.window.statusBarColor =
            color?.toColor(context) ?: activity.statusBarColor
    }

    protected open val withWindowsInsetsPaddingTop: Boolean = false

    open val onWindowInset: ((windowInsets: WindowInsetsCompat) -> Unit)? = null


    init {
        ScreensManager.backPressed.observe(lifecycleOwner = lifecycleOwner) {
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                onBackPressed?.invoke()
            }
        }

    }

}