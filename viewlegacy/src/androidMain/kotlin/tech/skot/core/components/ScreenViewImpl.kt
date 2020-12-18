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


abstract class ScreenViewProxy<I : ScreenViewImpl<out ViewBinding>> : ComponentViewProxy<I>(), ScreenView {

    val key = ScreensManager.addScreen(this)

    fun inflateAndLink(layoutInflater: LayoutInflater, activity: SKActivity, fragment: SKFragment?):View {
        val viewImpl = inflateAndLinkChildren(layoutInflater, activity, fragment)
        linkTo(viewImpl,fragment?.viewLifecycleOwner ?: activity )
        return viewImpl.view
    }

    abstract fun inflateAndLinkChildren(layoutInflater: LayoutInflater, activity: SKActivity, fragment: SKFragment?): I
//    {
//        val impl = createViewImplInstance(activity, fragment, layoutInflater)
//        linkTo(impl, fragment?.viewLifecycleOwner ?: activity)
//        return impl.view
//    }

//    abstract fun createViewImplInstance(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater): I

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