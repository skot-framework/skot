package tech.skot.components

import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import tech.skot.core.components.ComponentView
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKFragment
import tech.skot.core.components.UiState
import tech.skot.view.live.SKLiveData

abstract class ComponentViewProxy<B : Any> : ComponentView {

    override fun onRemove() {}

    protected val savedStates: MutableMap<String, Any> = mutableMapOf()

    abstract fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: B)

}

class UiStateImpl<D>:UiState<D> {
    override var value: D? = null
     fun bindTo(impl:ViewImplWithState<D>) {
         value?.let { impl.restoreState(it) }
         impl.lifecycle.addObserver(object : LifecycleObserver {
             @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
             fun onDestroyView() {
                 value = impl.saveState()
             }
         })
     }
}

interface ViewImplWithState<D>:LifecycleOwner {
    fun saveState():D
    fun restoreState(state:D)

}




abstract class ComponentViewImpl<B : Any>(protected val activity: SKActivity, protected val fragment: SKFragment?, val binding: B) : LifecycleOwner {

    val context = fragment?.context ?: activity

    protected val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    override fun getLifecycle() = fragment?.viewLifecycleOwner?.lifecycle ?: activity.lifecycle


    fun <D> SKLiveData<D>.observe(onChanged: (D) -> Unit) {
        observe(lifecycleOwner = this@ComponentViewImpl, onChanged)
    }





}
