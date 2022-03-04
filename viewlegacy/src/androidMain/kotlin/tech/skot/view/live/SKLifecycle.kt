package tech.skot.view.live

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

class SKLifecycle(private val parent: Lifecycle) : Lifecycle() {
    override fun addObserver(observer: LifecycleObserver) {
        parent.addObserver(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        parent.removeObserver(observer)
    }

    var recycled: Boolean = false

    override fun getCurrentState(): State {
        return if (recycled) {
            State.DESTROYED
        } else {
            parent.currentState
        }
    }

}

