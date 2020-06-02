package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import tech.skot.contract.modelcontract.MutablePoker
import tech.skot.contract.modelcontract.Poker
import tech.skot.core.SKLog
import tech.skot.core.currentTimeMillis

interface WithParameterDataManager<D : Any> {
    val updatePoker: Poker
    suspend fun getValue(id: String?, fresh: Boolean = false, speed: Boolean = false, updateIfSpeed: Boolean = true, cacheIfError: Boolean = true): D
    fun update(id: String?)

    suspend fun setDataStr(id: String?, newStrData: String, tmsp: Long? = null)
    suspend fun setData(id: String?, newData: D, tmsp: Long? = null)

    suspend fun invalidate()
}

open class WithParameterDataManagerImpl<D : Any>(
        private val key: String,
        private val cacheValidity: Long?,
        private val serializer: KSerializer<D>,
        private val cache: Persistor,
        private val onNewData: ((newData: D) -> Unit)? = null,
        private val getFreshStrData: suspend (id: String?) -> String
) : WithParameterDataManager<D> {

    private val json by lazy {
        Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
    }


    override val updatePoker = MutablePoker()

    private var _value: DatedData<D>? = null

    private fun DatedData<*>.isValid() = cacheValidity == null || ((timestamp + cacheValidity) > currentTimeMillis())

    override suspend fun getValue(id: String?, fresh: Boolean, speed: Boolean, updateIfSpeed: Boolean, cacheIfError: Boolean): D {
        if (_value?.id != id) {
            invalidate()
        }
        if (fresh) {
            return getFreshData(id)
        } else {
            val currenValue = _value
            if (currenValue?.isValid() == true) {
                return currenValue.data
            } else {
                val cachedStr = cache.getString(key)
                if (cachedStr != null) {
                    val isCachedStrValid = cachedStr.isValid()
                    if (speed || isCachedStrValid) {
                        val cachedData: D? =
                                try {
                                    json.parse(serializer, cachedStr.data)
                                } catch (ex: Exception) {
                                    SKLog.e("Problème au parse de la donnée $key en cache, le format a probablement changé", ex)
                                    null
                                }

                        if (cachedData != null) {
                            _value = DatedData(cachedData, id, cachedStr.timestamp)
                            if (speed && !isCachedStrValid && updateIfSpeed) {
                                update(id)
                            }
                            return cachedData
                        } else {
                            return getFreshData(if (cacheIfError) cachedStr.data else null)
                        }
                    } else {
                        return getFreshData(if (cacheIfError) cachedStr.data else null)
                    }
                } else {
                    return getFreshData(id)
                }


            }
        }

    }

    override suspend fun invalidate() {
        _value = null
        cache.remove(key)
    }

    override fun update(id: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            getFreshData(id)
        }
    }

    private val mutexRefresh = Mutex()

    private suspend fun getFreshData(id: String?, strCached: String? = null): D {
        val refreshDemandTmsp = currentTimeMillis()
        mutexRefresh.withLock {
            val currentValue = _value
            if (currentValue == null || currentValue.timestamp < refreshDemandTmsp) {
                return try {
                    val freshStrData = getFreshStrData(id)
                    val freshData = json.parse(serializer, freshStrData)
                    onNewData?.invoke(freshData)
                    val now = currentTimeMillis()
                    cache.putString(key, freshStrData, now)
                    _value = DatedData(freshData, id, now)
                    updatePoker.poke()
                    freshData
                } catch (ex: Exception) {
                    if (strCached != null) {
                        try {
                            json.parse(serializer, strCached)
                        } catch (exParseCache: Exception) {
                            throw ex
                        }
                    } else {
                        throw ex
                    }
                }
            } else {
                return currentValue.data
            }
        }
    }


    override suspend fun setDataStr(id: String?, newStrData: String, tmsp: Long?) {
        val date = tmsp ?: currentTimeMillis()
        val newData = json.parse(serializer, newStrData)
        cache.putString(key, newStrData, date)
        _value = DatedData(newData, id, date)
        onNewData?.invoke(newData)
        updatePoker.poke()
    }

    override suspend fun setData(id: String?, newData: D, tmsp: Long?) {
        val date = tmsp ?: currentTimeMillis()
        _value = DatedData(newData, id, date)
        cache.putString(key, json.stringify(serializer, newData), date)
        onNewData?.invoke(newData)
        updatePoker.poke()
    }

}