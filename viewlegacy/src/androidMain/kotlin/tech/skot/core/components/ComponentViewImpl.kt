package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import tech.skot.core.SKLog
import tech.skot.view.live.SKLiveData
import tech.skot.view.live.SKMessage
import java.lang.ref.WeakReference

abstract class ComponentViewImpl<B : Any>(protected val activity: SKActivity, protected val fragment: Fragment?, val binding: B) : LifecycleOwner {

    val context = fragment?.context ?: activity

    protected val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    override fun getLifecycle() = fragment?.viewLifecycleOwner?.lifecycle ?: activity.lifecycle

    var collectObservers = false

    private val componentObservers:MutableList<SKLiveData<*>.LifecycleOwnerObserver> by lazy {
        SKLog.d("---- ${this::class.simpleName} componentObservers initilized ")
        mutableListOf() }

    fun <D> SKLiveData<D>.observe(onChanged: (D) -> Unit) {
        observe(lifecycleOwner = this@ComponentViewImpl, onChanged).let {
            if (collectObservers) {
                componentObservers.add(it)
            }
        }
    }

    fun removeObservers() {
        componentObservers.forEach { it.remove() }
        componentObservers.clear()
    }

    fun <D> SKMessage<D>.observe(onReceive: (D) -> Unit) {
        observe(lifecycleOwner = this@ComponentViewImpl, onReceive)
    }

}
