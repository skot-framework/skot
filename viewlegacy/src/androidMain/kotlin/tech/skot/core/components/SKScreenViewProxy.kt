package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import tech.skot.core.view.Color
import tech.skot.core.view.Style
import tech.skot.view.live.MutableSKLiveData

abstract class SKScreenViewProxy<B : ViewBinding> : SKComponentViewProxy<B>(), SKScreenVC {

    protected abstract val visibilityListener: SKVisiblityListener

    val key = ScreensManager.addScreen(this)

    override fun onRemove() {
        super.onRemove()
        ScreensManager.removeScreen(this)
    }

    private val onBackPressedLD = MutableSKLiveData<(() -> Unit)?>(null)
    override var onBackPressed by onBackPressedLD

    private val statusBarColorLD = MutableSKLiveData<Color?>(null)
    override var statusBarColor: Color? by statusBarColorLD

    abstract override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: B
    ): SKScreenView<B>

    open fun getActivityClass(): Class<*> = SKActivity::class.java

    fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        layoutInflater: LayoutInflater
    ): SKScreenView<B> {
        return bindTo(activity, fragment, inflate(layoutInflater, null, false)).apply {
            onBackPressedLD.observe {
                setOnBackPressed(it)
            }
            displayMessageMessage.observe {
                displayMessage(it)
            }
            closeKeyboardMessage.observe {
                closeKeyboard()
            }
            requestPermissionsMessage.observe {
                requestPermissions(it)
            }
            statusBarColorLD.observe {
                onStatusBarColor(it)
            }
        }
    }


    abstract override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): B


    fun createFragment(): SKFragment =
        SKFragment().apply {
            arguments = Bundle().apply {
                putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
            }
        }

    fun createBottomSheetFragment(
        expanded: Boolean,
        skipCollapsed: Boolean
    ): SKBottomSheetDialogFragment =
        SKBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
                putBoolean(SK_BOTTOM_SHEET_DIALOG_EXPANDED, expanded)
                putBoolean(SK_BOTTOM_SHEET_DIALOG_SKIP_COLLAPSED, skipCollapsed)
            }
        }

    fun createDialogFragment(style : Style?): SKDialogFragment =
        SKDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
                if(style != null){
                    putInt(ScreensManager.SK_ARGUMENT_DIALOG_STYLE, style.res)
                }
            }
        }


    @CallSuper
    open fun onResume() {
        visibilityListener.onResume()
    }

    @CallSuper
    open fun onPause() {
        visibilityListener.onPause()
    }


}