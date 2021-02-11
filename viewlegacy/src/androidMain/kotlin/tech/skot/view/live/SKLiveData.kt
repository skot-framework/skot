package tech.skot.view.live

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class SKLiveData<D>(initialValue: D) : SKLiveDataCommon<D>(initialValue) {

    fun observe(lifecycleOwner: LifecycleOwner, onChanged: (d: D) -> Unit) =
        LifecycleOwnerObserver(lifecycleOwner, onChanged).also { observers.add(it) }


    fun setObserver(lifecycleOwner: LifecycleOwner, onChanged: (d: D) -> Unit) {
        val currentObservers = observers.toSet()
        currentObservers.forEach {
            (it as? LifecycleOwnerObserver)?.apply {
                onDestroy()
                lifecycleOwner.lifecycle.removeObserver(lifecycleOwnerObserver)
            }
        }
        observers.add(LifecycleOwnerObserver(lifecycleOwner, onChanged))
    }


    inner class LifecycleOwnerObserver(
            val lifecycleOwner: LifecycleOwner,
            onChanged: (d: D) -> Unit
    ) : Observer(onChanged) {

        private val liveData = this@SKLiveData
        fun remove() {
            liveData.removeObserver(this)
        }
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
                    newState.isAtLeast(Lifecycle.State.STARTED) -> onBecomeActive()
                    else -> onBecomeInactive()
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



fun <S, T> SKLiveData<S>.map(transformation: (S) -> T): MutableSKLiveData<T> =
        MediatorSKLiveData<T>(transformation(value)).apply {
            addSource(this@map) {
                postValue(transformation(it))
            }
        }