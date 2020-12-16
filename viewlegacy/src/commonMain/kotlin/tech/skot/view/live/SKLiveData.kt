package tech.skot.view.live


abstract class SKLiveDataCommon<D>(initialValue: D) {

    private var version = 0
    val observers: MutableSet<Observer> = mutableSetOf()


    var value: D = initialValue
        protected set(newVal) {
            if (newVal != field) {
                field = newVal
                version++
                notify(newVal, version)
            }
        }

    init {
        notify(initialValue, version)
    }

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

    fun notify(newValue: D, newVersion: Int) {
        observers.forEach {
            it.notify(newValue, newVersion)
        }
    }


    abstract inner class Observer(val onChanged: (d: D) -> Unit) {
        var lastVersion = -1
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
            if (lastVersion < version) {

                onChanged(this@SKLiveDataCommon.value)
                lastVersion = version
            }
        }

        fun onBecomeInactive() {
            mActive = false
        }

        fun onDestroy() {
            mActive = false
            observers.remove(this)
        }

        fun notify(value: D, valueVersion: Int) {
            if (!mActive) return
            if (!shouldBeActive()) {
                mActive = false
                return
            }
            if (lastVersion < valueVersion) {
                runOnMainThread { onChanged(value) }
                lastVersion = valueVersion
            }
        }
    }

    inner class AlwaysObserver(
            onChanged: (d: D) -> Unit
    ) : Observer(onChanged) {
        override fun shouldBeActive() = true

        fun plug() {
            observeForEver(this)
        }

        fun unPug() {
            removeObserver(this)
        }
    }

    fun observeForEver(
            onChanged: (d: D) -> Unit
    ): AlwaysObserver {
        val observer = AlwaysObserver(onChanged)
        observeForEver(observer)
        return observer
    }

    fun makeAlwaysObserver(
            onChanged: (d: D) -> Unit
    ) = AlwaysObserver(onChanged)

    fun observeForEver(observer: AlwaysObserver) {
        observers.add(observer)
        observer.onBecomeActive()
    }

    fun removeObserver(observer: AlwaysObserver) {
        observer.onBecomeInactive()
        observers.remove(observer)
    }

    fun debug() =
            "value: $value ${observers.size} observers dont ${observers.count { it.mActive }} actifs activeCount $activeCount"


}


expect open class SKLiveData<D>(initialValue: D) : SKLiveDataCommon<D>

open class MutableSKLiveData<D>(initialValue: D) : SKLiveData<D>(initialValue) {
    fun postValue(newVal: D) {
        this.value = newVal
    }
}

fun <S, T> SKLiveData<S>.map(transformation: (S) -> T): MutableSKLiveData<T> =
        MediatorSKLiveData<T>(transformation(value)).apply {
            addSource(this@map) {
                postValue(transformation(it))
            }
        }
