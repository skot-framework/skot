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
        CoroutineScope(Dispatchers.Default).launch {
            cache.putDataSecured(serializer, name, fetchedData.data, key, fetchedData.timestamp)
        }
        return fetchedData
    }

    private val initMutex = Mutex()

    private suspend fun initWithCache() {
        initMutex.withLock {
            if (flow.value == null) {
                flow.value = cache.getDataSecured(serializer, name, key)?.let { DatedData(it.data, it.timestamp) }
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


}
