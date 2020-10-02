package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.skot.core.Poker
import tech.skot.core.MutablePoker
import tech.skot.core.SKLog
import tech.skot.core.currentTimeMillis

interface WithParameterDataManager<D : Any> {
    val updatePoker: Poker
    suspend fun getValue(id: String, fresh: Boolean = false, speed: Boolean = false, updateIfSpeed: Boolean = false, cacheIfError: Boolean = true): D
    fun update(id: String)

    suspend fun setDataStr(id: String, newStrData: String, tmsp: Long? = null)
    suspend fun setData(id: String, newData: D, tmsp: Long? = null)

    suspend fun invalidate()
}

class WithParameterDataManagerImpl<D : Any>(
        key: String,
        cacheValidity: Long?,
        serializer: KSerializer<D>,
        cache: Persistor,
        onNewData: ((newData: D) -> Unit)? = null,
        getFreshStrData: suspend (id: String?) -> String
) : UnivDataManagerImpl<D>(key, cacheValidity, serializer, cache, onNewData, getFreshStrData), WithParameterDataManager<D> {
    override suspend fun getValue(id: String, fresh: Boolean, speed: Boolean, updateIfSpeed: Boolean, cacheIfError: Boolean) = univGetValue(id, fresh, speed, updateIfSpeed, cacheIfError)
    override fun update(id: String) {
        univUpdate(id)
    }

    override suspend fun setDataStr(id: String, newStrData: String, tmsp: Long?) {
        univSetDataStr(id, newStrData, tmsp)
    }

    override suspend fun setData(id: String, newData: D, tmsp: Long?) {
        univSetData(id, newData, tmsp)
    }
}

abstract class UnivDataManagerImpl<D : Any>(
        private val key: String,
        private val cacheValidity: Long?,
        private val serializer: KSerializer<D>,
        private val cache: Persistor,
        private val onNewData: ((newData: D) -> Unit)? = null,
        private val getFreshStrData: suspend (id: String?) -> String
) {

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }


    val updatePoker = MutablePoker()

    private var _value: DatedData<D>? = null

    private fun DatedData<*>.isValid() = cacheValidity == null || ((timestamp + cacheValidity) > currentTimeMillis())


    protected suspend fun univGetValue(id: String?, fresh: Boolean, speed: Boolean, updateIfSpeed: Boolean, cacheIfError: Boolean): D {
//        SKLog.d("UnivDataManagerImpl $key id $id fresh $fresh speed $speed updateIfSpeed $updateIfSpeed cacheIfError $cacheIfError")
        if (fresh) {
            return getFreshData(id)
        } else {
            val currenValue = _value
            if (currenValue?.isValid() == true && currenValue.id == id) {
                return currenValue.data
            } else {
                val cachedStr = cache.getString(key)
//                SKLog.d("UnivDataManagerImpl cachedStr!= null ${cachedStr!=null} cachedStr.id ${cachedStr?.id}")
                if (cachedStr != null && cachedStr.id == id) {
                    val isCachedStrValid = cachedStr.isValid()
//                    SKLog.d("UnivDataManagerImpl $key cachedStr!= null ${cachedStr!=null} isCachedStrValid $isCachedStrValid")
                    if (speed || isCachedStrValid) {
                        val cachedData: D? =
                                try {
                                    json.decodeFromString(serializer, cachedStr.data)
                                } catch (ex: Exception) {
                                    SKLog.e("Problème au parse de la donnée $key en cache, le format a probablement changé", ex)
                                    null
                                }

                        if (cachedData != null) {
                            _value = DatedData(cachedData, id, cachedStr.timestamp)
                            if (speed && !isCachedStrValid && updateIfSpeed) {
                                univUpdate(id)
                            }
                            return cachedData
                        } else {
                            return getFreshData(id = id, strCached = if (cacheIfError) cachedStr.data else null)
                        }
                    } else {
                        return getFreshData(id = id, strCached = if (cacheIfError) cachedStr.data else null)
                    }
                } else {
                    return getFreshData(id = id)
                }


            }
        }

    }

    suspend fun invalidate() {
//        SKLog.d("UnivDataManagerImpl $key invalidate ")
        _value = null
        cache.remove(key)
    }


    protected fun univUpdate(id: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                getFreshData(id = id)
            } catch (ex: Exception) {
                SKLog.e("erreur à la mise à jour suite à utilisation du cache en speed", ex)
            }
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
                    val freshData = json.decodeFromString(serializer, freshStrData)
                    onNewData?.invoke(freshData)
                    val now = currentTimeMillis()
//                    SKLog.d("UnivDataManagerImpl ${key }will put cache data: $freshData ")
                    cache.putString(key, id, freshStrData, now)
                    _value = DatedData(freshData, id, now)
                    updatePoker.poke()
                    freshData
                } catch (ex: Exception) {
                    if (strCached != null) {
                        try {
                            json.decodeFromString(serializer, strCached)
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


    protected suspend fun univSetDataStr(id: String?, newStrData: String, tmsp: Long?) {
        val date = tmsp ?: currentTimeMillis()
        val newData = json.decodeFromString(serializer, newStrData)
        cache.putString(key, id, newStrData, date)
        _value = DatedData(newData, id, date)
        onNewData?.invoke(newData)
        updatePoker.poke()
    }


    protected suspend fun univSetData(id: String?, newData: D, tmsp: Long?) {
        val date = tmsp ?: currentTimeMillis()
        _value = DatedData(newData, id, date)
        cache.putString(key, id, json.encodeToString(serializer, newData), date)
        onNewData?.invoke(newData)
        updatePoker.poke()
    }

}