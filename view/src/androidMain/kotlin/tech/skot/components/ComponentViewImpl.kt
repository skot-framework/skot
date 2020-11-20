package tech.skot.components

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import tech.skot.contract.view.ComponentView
import tech.skot.view.Action
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<A : AppCompatActivity, F : Fragment, B : Any> : ComponentView {

    protected val messages =
            SKMessage<Action>()

    override fun onRemove() {
    }

    protected var _activity:A? = null
    var activity: A
        get() = _activity!!
        set(value) {
            _activity = value
        }

    var fragment: F? = null

    protected var _binding:B? = null
    var binding: B
        get() = _binding!!
        set(value) {
            _binding = value
        }

    //doit appeler les initWith des sous-composants
    open fun initWith(activity: A, fragment: F?, binding:B) {
        this.activity = activity
        this.fragment = fragment
        this.binding = binding
        onInflated()
    }

    @CallSuper
    open fun onInflated() {
    }

    //doit appeler les linkTo des sous-composants
    @CallSuper
    open fun linkTo(lifecycleOwner: LifecycleOwner) {
        messages.observe(lifecycleOwner) {
            treatAction(it)
        }
    }

    fun <PA:A, PF:F> linkLately(parentComponent:ComponentViewImpl<PA,F,*>, binding: B) {
        initWith(parentComponent.activity, parentComponent.fragment, binding)
        linkTo(parentComponent.lifeCycleOwner)
    }

    private val onCleanViewReferencesActions: MutableList<()->Unit> = mutableListOf()
    fun addOnCleanViewReferences(action:()->Unit) {
        onCleanViewReferencesActions.add(action)
    }


    @CallSuper
    open fun cleanViewReferences() {
        onCleanViewReferencesActions.forEach { it.invoke() }
        onCleanViewReferencesActions.clear()
        _activity = null
        fragment = null
        _binding = null
    }

    protected open fun treatAction(action: Action) {
    }

    val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    val lifeCycleOwner: LifecycleOwner
        get() = fragment?.viewLifecycleOwner ?: activity
}