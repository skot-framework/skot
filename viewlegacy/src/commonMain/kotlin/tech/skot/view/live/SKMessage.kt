package tech.skot.view.live

abstract class SKMessageCommon<D> {

    private var pendingValues: List<D> = emptyList()

    private var activeCount = 0
        set(value) {
            if (field == 0 && value > 0) {
                onActive()
            }
            if (value == 0 && field > 0) {
                onInactive()
            }
            field = value
        }

    //called on transition 0 active observer to 1
    protected open fun onActive() = Unit

    //called on transition 1 active observer to 0
    protected open fun onInactive() = Unit

    protected open fun hasActiveObserver() = activeCount > 0


    val observers: MutableSet<Observer> = mutableSetOf()


    abstract inner class Observer(val onChanged: (d: D) -> Unit) {
        var mActive: Boolean = false
            set(value) {
                if (field != value) {
                    activeCount += if (value) 1 else -1
                }
                field = value
            }

        abstract fun shouldBeActive(): Boolean

        fun onBecomeActive() {
            mActive = true
            pendingValues.forEach {
                pendingValues = pendingValues - it
                onChanged(it)
            }
        }


        fun onBecomeInactive() {
            mActive = false
        }

        fun onDestroy() {
            mActive = false
            observers.remove(this)
        }

        fun notify(value: D): Boolean {
            if (!mActive) return false
            if (!shouldBeActive()) {
                mActive = false
                return false
            }
            pendingValues = pendingValues - value
            onChanged(value)
            return true
        }
    }

    inner class AlwaysObserver(onChanged: (d: D) -> Unit) : Observer(onChanged) {
        override fun shouldBeActive() = true

        fun plug() {
            observeForEver(this)
        }

        fun unPug() {
            removeObserver(this)
        }
    }

    fun observeForEver(onChanged: (d: D) -> Unit): AlwaysObserver {
        val observer = AlwaysObserver(onChanged)
        observeForEver(observer)
        return observer
    }

    fun makeAlwaysObserver(onChanged: (d: D) -> Unit) = AlwaysObserver(onChanged)

    fun observeForEver(observer: AlwaysObserver) {
        observers.add(observer)
        observer.onBecomeActive()
    }

    fun removeObserver(observer: AlwaysObserver) {
        observer.onBecomeInactive()
        observers.remove(observer)
    }

    fun debug() =
            "pendingValues: $pendingValues ${observers.size} observers dont ${observers.count { it.mActive }} actifs activeCount $activeCount"


    fun post(message: D) {
        runOnMainThread {
            pendingValues = pendingValues + message
            for (observer in observers) {
                if (observer.notify(message)) break
            }
        }
    }

}


expect class SKMessage<D>() : SKMessageCommon<D>