package tech.skot.core.components

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.OnLifecycleEvent

abstract class ComponentViewProxy<B : Any> : ComponentVC {
//    protected val savedStates: MutableMap<String, Any> = mutableMapOf()

    abstract fun bindTo(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater, binding: B): ComponentViewImpl<B>

    open fun saveState() {
        //surchargée quand le component a un état à sauver
    }

    open val layoutId = -1

    open fun bind(view:View):B {
        throw IllegalStateException("You cant't bind this component to a view")
    }

    fun bindToItemView(activity: SKActivity, fragment: Fragment?, layoutInflater: LayoutInflater, view:View) {
        bindTo(activity, fragment, layoutInflater, bind(view))
    }
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
