package tech.skot.view.live

actual open class SKLiveData<D> actual constructor(initialValue:D) : SKLiveDataCommon<D>(initialValue) {


    fun observe(viewModelStore: ViewModelStore, onChanged: (d: D?) -> Unit) {

        observers.add(object : Observer(onChanged) {
            init {
                viewModelStore.observers.add(object :
                        ViewModelStore.LifeCycleObserver {
                    override fun activated() {
                        onBecomeActive()
                    }

                    override fun willDestroy() {
                        onDestroy()
                    }

                    override fun willDesactivate() {
                        onBecomeInactive()
                    }
                })
            }

            override fun shouldBeActive(): Boolean {
                return viewModelStore.isActive()
            }

        })

    }


}