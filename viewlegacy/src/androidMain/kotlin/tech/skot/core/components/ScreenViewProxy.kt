package tech.skot.core.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import tech.skot.view.live.MutableSKLiveData

abstract class ScreenViewProxy<B : ViewBinding> : ComponentViewProxy<B>(), ScreenVC {

    val key = ScreensManager.addScreen(this)

    private val onBackPressedLD = MutableSKLiveData<(() -> Unit)?>(null)
    override var onBackPressed by onBackPressedLD

    abstract override fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: B): ScreenViewImpl<B>

    open fun getActivityClass():Class<*> = SKActivity::class.java

    fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater): View {
        val binding = inflate(layoutInflater)
        bindTo(activity, fragment, layoutInflater, binding).apply {
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

}