package tech.skot.core.components

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import tech.skot.view.extensions.updatePadding


abstract class SKScreenView<B : ViewBinding>(activity: SKActivity, fragment: Fragment?, binding: B) : SKComponentView<B>(activity, fragment, binding) {
    val view: View = binding.root

    open fun windowInsetPaddingTop() = false

    private var onBackPressed: (() -> Unit)? = null
    fun setOnBackPressed(onBackPressed: (() -> Unit)?) {
        this.onBackPressed = onBackPressed
    }

    open fun onResume() {
//        SKLog.d("${this::class.simpleName} ${this.hashCode()} onResume")
    }

    open fun onPause() {
//        SKLog.d("${this::class.simpleName} ${this.hashCode()} onPause")
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
            val initialPaddingTop = view.paddingTop
            if (loadedInsets != null) {
                view.updatePadding(top = view.paddingTop + loadedInsets.systemWindowInsetTop)
            } else {
                view.setOnApplyWindowInsetsListener { view, windowInsets ->
                    view.updatePadding(top = initialPaddingTop + windowInsets.systemWindowInsetTop)
                    windowInsets
                }
            }
        }

    }

}