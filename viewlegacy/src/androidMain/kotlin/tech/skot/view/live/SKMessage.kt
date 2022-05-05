package tech.skot.view.live

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import tech.skot.core.SKLog

class SKMessage<D>(multiReceiver:Boolean = false) : SKMessageCommon<D>(multiReceiver) {

    fun observe(lifecycleOwner: LifecycleOwner, onChanged: (d: D) -> Unit) {
        observers.add(LifecycleOwnerObserver(lifecycleOwner, onChanged))
    }

    fun setObserver(lifecycleOwner: LifecycleOwner, onChanged: (d: D) -> Unit): LifecycleOwnerObserver {
        val currentObservers = observers.toSet()
        currentObservers.forEach {
            (it as? LifecycleOwnerObserver)?.apply {
                onDestroy()
                lifecycleOwner.lifecycle.removeObserver(lifecycleOwnerObserver)
            }
        }
        return LifecycleOwnerObserver(lifecycleOwner, onChanged).also { observers.add(it) }
    }

    inner class LifecycleOwnerObserver(
            val lifecycleOwner: LifecycleOwner,
            onChanged: (d: D) -> Unit
    ) : Observer(onChanged) {

        init {
            if (shouldBeActive()) {
                onBecomeActive()
            }
        }

        val lifecycleOwnerObserver = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny() {
                val newState = lifecycleOwner.lifecycle.currentState
                when {
                    newState == Lifecycle.State.DESTROYED -> {
                        onDestroy()
                        lifecycleOwner.lifecycle.removeObserver(this)
                    }
                    newState.isAtLeast(Lifecycle.State.STARTED) -> {
                        onBecomeActive()
                    }
                    else -> {
                        onBecomeInactive()
                    }
                }
            }
        }
        init {
            lifecycleOwner.lifecycle.addObserver(lifecycleOwnerObserver)
        }

        override fun shouldBeActive(): Boolean {
            return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        }

    }


}
