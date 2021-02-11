package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import tech.skot.view.live.MutableSKLiveData

abstract class ScreenViewProxy<B : ViewBinding> : ComponentViewProxy<B>(), ScreenVC {

    val key = ScreensManager.addScreen(this)

    private val onBackPressedLD = MutableSKLiveData<(() -> Unit)?>(null)
    override var onBackPressed by onBackPressedLD

    abstract override fun bindTo(activity: SKActivity, fragment: Fragment?, binding: B, collectingObservers:Boolean): ScreenViewImpl<B>

    open fun getActivityClass(): Class<*> = SKActivity::class.java

    fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater): View {
        val binding = inflate(layoutInflater)
        bindTo(activity, fragment, binding).apply {
            onBackPressedLD.observe {
                setOnBackPressed(it)
            }
        }
        return binding.root
    }


    abstract fun inflate(layoutInflater: LayoutInflater): B


    fun createFragment(): SKFragment =
            SKFragment().apply {
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

}