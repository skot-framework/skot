package tech.skot.core.components

import android.view.LayoutInflater
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.OnLifecycleEvent

abstract class ComponentViewProxy<B : Any> : ComponentVC {
//    protected val savedStates: MutableMap<String, Any> = mutableMapOf()

    abstract fun bindTo(activity: SKActivity, fragment: SKFragment?, layoutInflater: LayoutInflater, binding: B): ComponentViewImpl<B>

}

//class UiStateImpl<D>:UiState<D> {
//    override var value: D? = null
//    fun bindTo(impl: ViewImplWithState<D>) {
//        value?.let { impl.restoreState(it) }
//        impl.lifecycle.addObserver(object : LifecycleObserver {
//            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//            fun onDestroyView() {
//                value = impl.saveState()
//            }
//        })
//    }
//}
//
//interface ViewImplWithState<D>: LifecycleOwner {
//    fun saveState():D
//    fun restoreState(state:D)
//
//}
