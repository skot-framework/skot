package tech.skot.components

import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import tech.skot.view.MaxSKActivity
import tech.skot.view.MaxSKFragment
import tech.skot.view.extensions.updatePadding

abstract class MaxScreenViewImpl<A : MaxSKActivity, F : MaxSKFragment, B : ViewBinding> :
        ScreenViewImpl<A, F, B>()
{

    override fun onOnBack(onBack: (() -> Unit)?) {
        if (bottomSheetDialog == null) {
            activity.onBackPressedAction = onBack
        }
    }

    override fun onLoading(loading: Boolean) {
        TODO("not implemented for this screen ${this::class.java.simpleName}")
    }


    @CallSuper
    open fun onPause() {
    }

    @CallSuper
    open fun onResume() {
        if (bottomSheetDialog == null) {
            activity.onBackPressedAction = onBack
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val nightMode = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

            if (setStatusBarWhiteText() || nightMode) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }


            val basePaddingTop = (initialPaddingTop
                    ?: binding.root.paddingTop.apply { initialPaddingTop = this })

            val loadedInsets = activity.window?.decorView?.rootWindowInsets
            if (loadedInsets != null) {
                onWindowInsets(loadedInsets, basePaddingTop)
            } else {
                binding.root.setOnApplyWindowInsetsListener { view, windowInsets ->
                    onWindowInsets(windowInsets, basePaddingTop)
                    windowInsets
                }
            }

        }
    }

    @CallSuper
    open fun onWindowInsets(windowInsets: WindowInsets, basePaddingTop: Int) {
        if (setPaddingTopStatusBar()) {
            binding.root.updatePadding(top = windowInsets.systemWindowInsetTop + basePaddingTop)
        }
    }

    open fun setStatusBarWhiteText(): Boolean = false
    open fun setPaddingTopStatusBar(): Boolean = false

    private var oldSystemUiVisibility: Int? = null

    private var initialPaddingTop: Int? = null

    override fun onInflated() {
        super.onInflated()

        if (bottomSheetDialog == null) {
            fragment?.let {
                it.onPauseLambda = { onPause() }
                it.onResumeLambda = { onResume() }
            } ?: activity.let {
                it.onPauseLambda = { onPause() }
                it.onResumeLambda = { onResume() }
            }
        }

    }
}