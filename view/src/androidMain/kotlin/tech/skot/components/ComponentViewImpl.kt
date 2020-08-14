package tech.skot.components

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import tech.skot.core.SKLog
import tech.skot.view.Action
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<A : AppCompatActivity, F : Fragment, B : Any> : ComponentView {

    protected val messages =
            SKMessage<Action>()

    override fun onRemove() {
    }

    lateinit var activity: A
    var fragment: F? = null
    lateinit var binding: B

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


    protected open fun treatAction(action: Action) {
    }

    val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager

    val lifeCycleOwner: LifecycleOwner
        get() = fragment ?: activity
}