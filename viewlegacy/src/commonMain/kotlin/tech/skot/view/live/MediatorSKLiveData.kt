package tech.skot.view.live

class MediatorSKLiveData<D>(initialValue: D) : MutableSKLiveData<D>(initialValue) {


    private val sources: MutableMap<SKLiveData<*>, SKLiveDataCommon<*>.AlwaysObserver> = mutableMapOf()


    fun <S> addSource(liveData: SKLiveData<S>, onChanged: (s: S) -> Unit) {
        val observer = liveData.makeAlwaysObserver(onChanged)
        sources[liveData] = observer
        if (hasActiveObserver()) {
            observer.plug()
        }
    }

    fun <S> removeSource(liveData: SKLiveData<S>) {
        sources.remove(liveData)?.unPug()
    }

    override fun onActive() {
        sources.values.forEach { it.plug() }
    }

    override fun onInactive() {
        sources.values.forEach { it.unPug() }
    }

}