package tech.skot.components

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import tech.skot.view.Action
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<A : AppCompatActivity, F : Fragment> : ComponentView {

    protected val messages =
            SKMessage<Action>()

    override fun onRemove() {
    }


    open fun linkTo(lifecycleOwner: LifecycleOwner) {
        messages.observe(lifecycleOwner) {
            treatAction(it)
        }
    }

    protected open fun treatAction(action: Action) {
    }

}