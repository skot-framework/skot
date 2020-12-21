package tech.skot.view.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.core.components.ScreenView
import tech.skot.core.components.ScreensManager


abstract class ScreenViewProxy<I : ViewBinding> : ComponentViewProxy<I>(), ScreenView {

    val key = ScreensManager.addScreen(this)

    fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater):View {
        val binding = inflate(layoutInflater)
        bindTo(activity, fragment, layoutInflater, binding)
        return binding.root
    }

    abstract fun inflate(layoutInflater:LayoutInflater):I

    fun createFragment(): SKFragment =
            SKFragment().apply {
                arguments = Bundle().apply {
                    putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
                }
            }
}

abstract class ScreenViewImpl<B : ViewBinding>(activity: SKActivity, fragment: SKFragment?, binding: B) : ComponentViewImpl<B>(activity, fragment, binding) {
    val view: View = binding.root
}