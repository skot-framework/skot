package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.view.live.MutableSKLiveData

abstract class SKScreenViewProxy<B : ViewBinding> : SKComponentViewProxy<B>(), SKScreenVC {

    val key = ScreensManager.addScreen(this)

    private val onBackPressedLD = MutableSKLiveData<(() -> Unit)?>(null)
    override var onBackPressed by onBackPressedLD

    abstract override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: B, collectingObservers:Boolean): SKScreenView<B>

    open fun getActivityClass(): Class<*> = SKActivity::class.java

    fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater): SKScreenView<B> {
        return bindTo(activity, fragment, inflate(layoutInflater, null, false), false).apply {
            onBackPressedLD.observe {
                setOnBackPressed(it)
            }
            displayErrorMessage.observe {
                displayError(it)
            }
        }
//        return binding.root
    }


    abstract override fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent:Boolean): B


    fun createFragment(): SKFragment =
            SKFragment().apply {
                SKLog.d("ééééééé SKScreenViewProxy  ${this@SKScreenViewProxy::class.simpleName} createFragment ")
                arguments = Bundle().apply {
                    putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
                }
            }

    fun createDialogFragment(): SKBottomSheetDialogFragment =
            SKBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
                }
            }


    override var onResume: (() -> Unit)? = null
    override var onPause: (() -> Unit)? = null
}