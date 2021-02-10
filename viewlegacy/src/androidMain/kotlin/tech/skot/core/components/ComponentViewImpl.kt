package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import tech.skot.view.live.SKLiveData
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<B : Any>(protected val activity: SKActivity, protected val fragment: Fragment?, val binding: B) : LifecycleOwner {

    val context = fragment?.context ?: activity

    protected val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    override fun getLifecycle() = fragment?.viewLifecycleOwner?.lifecycle ?: activity.lifecycle


    fun <D> SKLiveData<D>.observe(onChanged: (D) -> Unit) {
        observe(lifecycleOwner = this@ComponentViewImpl, onChanged)
    }

    fun <D> SKMessage<D>.observe(onReceive: (D) -> Unit) {
        observe(lifecycleOwner = this@ComponentViewImpl, onReceive)
    }

}
