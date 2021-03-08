package tech.skot.core.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.OnLifecycleEvent

abstract class ComponentViewProxy<B : Any> : ComponentVC {
//    protected val savedStates: MutableMap<String, Any> = mutableMapOf()

    abstract fun bindTo(activity: SKActivity, fragment: Fragment?, binding: B, collectingObservers:Boolean = false): ComponentViewImpl<B>

    open fun saveState() {
        //surchargée quand le component a un état à sauver
    }

    open val layoutId:Int? = null

    open fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent:Boolean): B {
        TODO("inflate method not implemented")
    }

    fun bindToView(activity: SKActivity, fragment: Fragment?, view: View, collectingObservers:Boolean = false) =
            bindTo(activity, fragment, bindingOf(view), collectingObservers)

    fun inflateInParentAndBind(activity: SKActivity, fragment: Fragment?, parent: ViewGroup) {
        val inflater = fragment?.layoutInflater ?: activity.layoutInflater
        bindTo(activity, fragment, inflate(inflater, parent, true), false)
    }

    open fun bindingOf(view:View):B {
        throw IllegalStateException("You cant't bind this component to a view")
    }

    fun bindToItemView(activity: SKActivity, fragment: Fragment?, view:View):ComponentViewImpl<B> {
        if (layoutId == null) {
            throw IllegalStateException("You cant't bind this component to an Item's view, it has no layout Id")
        }
        else {
            return bindTo(activity, fragment, bindingOf(view), collectingObservers = true)
        }
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
