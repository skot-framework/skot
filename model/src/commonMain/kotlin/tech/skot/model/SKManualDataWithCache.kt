package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import tech.skot.core.SKLog


abstract class SKManualDataWithCache<D : Any>(
    private val name: String,
    private val serializer: KSerializer<D>,
    private val key: String? = null,
    private val cache: SKPersistor = globalCache,
    private val initialDefaultValue: D
) : SKData<D> {

    override val flow: MutableStateFlow<DatedData<D>?> = MutableStateFlow(null)
    override val defaultValidity = Long.MAX_VALUE
    override val _current: DatedData<D>?
        get() = flow.value


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
                        SKLog.e(ex, "SKManualDataWithCache Problème à la récupération du cache de la donnée $name $key")
                    }

                }
            }
        }
    }

    override suspend fun fallBackValue(): D? {
        if (flow.value == null) {
            initWithCache()
        }
        return _current?.data
    }

    override suspend fun get(validity: Long?): D {
        if (flow.value == null) {
            initWithCache()
        }
        return super.get(validity)
    }


    fun setValue(newValue: D) {
        flow.value = DatedData(newValue)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                cache.putData(
                    serializer = serializer,
                    name = name,
                    data = newValue,
                    key = key
                )
            }
            catch (ex:Exception) {
                SKLog.e(ex, "SKManualDataWithCache Problème à la mise en cache de la donnée $name $key")
            }
        }


    }

    override suspend fun update(): D {
        if (flow.value == null) {
            initWithCache()
        }
        return _current?.data ?: initialDefaultValue
    }


}


