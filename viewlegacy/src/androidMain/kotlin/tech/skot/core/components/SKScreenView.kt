package tech.skot.core.components

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.CallSuper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
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
       activity.setFullScreen(fullScreen, lightStatusBar, onWindowInset ?: (if (withWindowsInsetsPaddingTop) {{
           view.updatePadding(top = originalPaddingTop + it.systemBars().top)
       }} else null))
        proxy.onResume?.invoke()

    }

    @CallSuper
    open fun onPause() {
        proxy.onPause?.invoke()
    }



    open val fullScreen: Boolean = false
    open val lightStatusBar: Boolean = true

    protected open val withWindowsInsetsPaddingTop : Boolean = false

    open val onWindowInset: ((windowInsets: WindowInsetsCompat) -> Unit)? = null


    init {
        ScreensManager.backPressed.observe(this) {
            onBackPressed?.invoke()
        }

    }

}