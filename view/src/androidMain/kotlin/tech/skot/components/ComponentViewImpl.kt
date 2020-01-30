package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import tech.skot.view.Action
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<A : AppCompatActivity, F : Fragment, B : ViewBinding> : ComponentView {

    protected val messages =
            SKMessage<Action>()

    override fun onRemove() {
    }

    lateinit var activity: A
    var fragment: F? = null
    lateinit var binding: B

    //doit appeler les initWith des sous-composants
    open fun initWith(activity: A, fragment: F?, binding:B) {
        this.activity = activity as A
        this.fragment = fragment as F?
        this.binding = binding
    }

    //doit appeler les linkTo des sous-composants
    open fun linkTo(lifecycleOwner: LifecycleOwner) {
        messages.observe(lifecycleOwner) {
            treatAction(it)
        }
    }

    protected open fun treatAction(action: Action) {
    }

    val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager
}