package tech.skot.core.components

import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
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
            SKLog.d("&&&&&&&&&& SKScreenView onResume  ${this::class.simpleName}  will call activity onResume")
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
        proxy.onResume()

    }

    @CallSuper
    open fun onPause() {
        proxy.onPause()
    }


    open val fullScreen: Boolean = false
    open val lightStatusBar: Boolean = true

    protected open val withWindowsInsetsPaddingTop: Boolean = false

    open val onWindowInset: ((windowInsets: WindowInsetsCompat) -> Unit)? = null


    init {
        ScreensManager.backPressed.observe(this) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                onBackPressed?.invoke()
            }
        }

    }

}