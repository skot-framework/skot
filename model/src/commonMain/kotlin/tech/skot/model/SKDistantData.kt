package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import tech.skot.core.SKLog

open class SKDistantData<D : Any>(validity: Long? = null, private val fetchData: suspend () -> D) : SimpleSKData<D>() {
    override suspend fun newDatedData() = DatedData(fetchData(), currentTimeMillis())
    override val defaultValidity = validity ?: super.defaultValidity
}

abstract class SKDistantDataWithCache<D : Any>(
        private val name: String,
        private val serializer: KSerializer<D>,
        protected open val key: String?,
        private val cache: SKPersistor = globalCache,
        validity: Long? = null,
        private val fetchData: suspend () -> D) : SKDistantData<D>(validity, fetchData) {



    override suspend fun newDatedData(): DatedData<D> {
        val fetchedData = DatedData(fetchData(), currentTimeMillis())
       saveInCache(fetchedData)
        return fetchedData
    }

    protected open val completeKey:String? = key

    private fun saveInCache(newData:DatedData<D>) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                cache.putData(serializer, name, newData.data, completeKey, newData.timestamp)
            }
            catch (ex:Exception) {
                SKLog.e(ex, "SKDistantDataWithCache Problème à la mise en cache de la donnée $name $key")
            }
        }
    }

    private val initMutex = Mutex()

    private suspend fun initWithCache() {
        initMutex.withLock {
            if (flow.value == null) {
                val cacheDate = cache.getDateOfData(name, key)
                if (cacheDate != null) {
                    try {
                        flow.value = cache.getData(serializer, name, key)?.let { DatedData(it, cacheDate) }
                    }
                    catch (ex:Exception) {
                        SKLog.e(ex, "SKDistantDataWithCache Problème à la récupération du cache de la donnée $name $key")
                    }

                }

            }
        }
    }

    override suspend fun fallBackValue(): D? {
        if (flow.value == null) {
            initWithCache()
        }
        return super.fallBackValue()
    }

    override suspend fun get(validity: Long?): D {
        if (flow.value == null) {
            initWithCache()
        }
        return super.get(validity)
    }

    override fun setDatedData(newDatedData: DatedData<D>) {
        super.setDatedData(newDatedData)
        saveInCache(newDatedData)
    }

}

abstract class SKDistantDataWithCacheAndLiveKey<D:Any>(
    name: String,
    serializer: KSerializer<D>,
    private val cache: SKPersistor = globalCache,
    validity: Long? = null,
    private val fixKey:String?,
    private val liveKey: () -> String,
    fetchData: suspend () -> D
):SKDistantDataWithCache<D>(
    name = name,
    serializer = serializer,
    key = "${fixKey}_${liveKey()}",
    cache = cache,
    validity = validity,
    fetchData = fetchData
) {

    override suspend fun get(validity: Long?): D {
        if (_current != null && liveKeyOfCurrentValue != liveKey()) {
            update()
        }
        return super.get(validity)
    }

    override val completeKey: String?
        get() = "${fixKey}_${liveKey()}"

    private var liveKeyOfCurrentValue: String? = null
    override fun setDatedData(newDatedData: DatedData<D>) {
        liveKeyOfCurrentValue = liveKey()
        super.setDatedData(newDatedData)
    }

    override suspend fun newDatedData(): DatedData<D> {
        return super.newDatedData().also { liveKeyOfCurrentValue = liveKey() }
    }

    override suspend fun fallBackValue(): D? {
        if (liveKeyOfCurrentValue == liveKey()) {
            return super.fallBackValue()
        } else {
            return null
        }
    }
}