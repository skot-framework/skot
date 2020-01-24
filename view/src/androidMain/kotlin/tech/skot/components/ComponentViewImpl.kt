package tech.skot.components

import androidx.lifecycle.LifecycleOwner
import tech.skot.view.Action
import tech.skot.view.SKActivity
import tech.skot.view.SKFragment
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<O : ComponentObserverInterface, A : SKActivity, F : SKFragment> : ComponentView {

    protected val messages =
            SKMessage<Action>()

    protected var onRemoveAction: (() -> Unit)? = null
    override fun onRemove() {
        onRemoveAction?.invoke()
    }


    open fun linkTo(observer: O, lifecycleOwner: LifecycleOwner) {
        onRemoveAction = { observer.onRemove() }

        messages.observe(lifecycleOwner) {
            treatAction(it, observer)
        }
    }

    protected open fun treatAction(action: Action, observer: O) {
    }

}