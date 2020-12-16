package tech.skot.view.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import tech.skot.components.ComponentViewImpl
import tech.skot.components.ComponentViewProxy
import tech.skot.contract.view.ScreenView
import tech.skot.core.SKLog


abstract class ScreenViewProxy<I : ScreenViewImpl<out ViewBinding>> : ComponentViewProxy<I>(), ScreenView {

    val key = ScreensManager.addScreen(this)

    fun inflate(layoutInflater: LayoutInflater, activity: SKActivity, fragment: SKFragment?): View {
        val impl = createViewImplInstance(activity, fragment, layoutInflater)
        linkTo(impl, fragment?.viewLifecycleOwner ?: activity)
        return impl.view
    }

    abstract fun createViewImplInstance(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater): I

    fun createFragment():SKFragment =
            SKFragment().apply {
                arguments = Bundle().apply {
                    putLong(ScreensManager.SK_ARGUMENT_VIEW_KEY, key)
                }
            }
}

abstract class ScreenViewImpl<B : ViewBinding>(activity: SKActivity, fragment: SKFragment?, binding: B) : ComponentViewImpl<B>(activity, fragment, binding) {
    val view: View = binding.root
}