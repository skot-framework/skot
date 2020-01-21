package tech.skot.view.component

import androidx.lifecycle.LifecycleOwner
import tech.skot.contract.viewcontract.ComponentView
import tech.skot.view.Action
import tech.skot.view.live.SKMessage

abstract class ComponentViewImpl<O : ComponentObserverInterface> : ComponentView {

    protected val messages =
            SKMessage<Action>()

    var onRemoveAction: (() -> Unit)? = null
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