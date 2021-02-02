package tech.skot.core.components

import android.view.View
import androidx.viewbinding.ViewBinding
import tech.skot.view.extensions.updatePadding


abstract class ScreenViewImpl<B : ViewBinding>(activity: SKActivity, fragment: SKFragment?, binding: B) : ComponentViewImpl<B>(activity, fragment, binding) {
    val view: View = binding.root

    open fun windowInsetPaddingTop() = false

    private var onBackPressed: (() -> Unit)? = null
    fun setOnBackPressed(onBackPressed: (() -> Unit)?) {
        this.onBackPressed = onBackPressed
    }

    init {
        ScreensManager.backPressed.observe(this) {
            onBackPressed?.invoke()
        }

        if (windowInsetPaddingTop()) {
            val loadedInsets = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                activity.window?.decorView?.rootWindowInsets
            } else {
                TODO("VERSION.SDK_INT < M")
            }
            if (loadedInsets != null) {
                view.updatePadding(top = view.paddingTop + loadedInsets.systemWindowInsetTop)
            } else {
                view.setOnApplyWindowInsetsListener { view, windowInsets ->
                    view.updatePadding(top = view.paddingTop + windowInsets.systemWindowInsetTop)
                    windowInsets
                }
            }
        }

    }

}