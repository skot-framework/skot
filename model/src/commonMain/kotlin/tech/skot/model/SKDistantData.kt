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

open class SKDistantDataWithCache<D : Any>(
        private val name: String,
        private val serializer: KSerializer<D>,
        private val key: String? = null,
        private val cache: SKPersistor = globalCache,
        validity: Long? = null,
        private val fetchData: suspend () -> D) : SKDistantData<D>(validity, fetchData) {

    override suspend fun newDatedData(): DatedData<D> {
        val fetchedData = DatedData(fetchData(), currentTimeMillis())
       saveInCache(fetchedData)
        return fetchedData
    }

    private fun saveInCache(newData:DatedData<D>) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                cache.putData(serializer, name, newData.data, key, newData.timestamp)
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
                val cacheDate = cache.getDate(name, key)
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
