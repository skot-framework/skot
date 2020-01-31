package tech.skot.model

import kotlinx.serialization.KSerializer
import tech.skot.contract.modelcontract.Poker


interface DataManager<D : Any> {
    val updatePoker: Poker
    suspend fun getValue(fresh: Boolean = false, speed: Boolean = false, updateIfSpeed: Boolean = true, cacheIfError: Boolean = true): D
    fun update()

    suspend fun setDataStr(newStrData: String, tmsp: Long? = null)
    suspend fun setData(newData: D, tmsp: Long? = null)
}

class DataManagerImpl<D : Any>(
        key: String,
        cacheValidity: Long,
        serializer: KSerializer<D>,
        cache: Persistor,
        onNewData: ((newData: D) -> Unit)? = null,
        getFreshStrData: suspend () -> String
) : WithParameterDataManagerImpl<D>(
        key = key,
        cacheValidity = cacheValidity,
        serializer = serializer,
        cache = cache,
        onNewData = onNewData,
        getFreshStrData = { getFreshStrData() }
), DataManager<D> {
    override suspend fun getValue(fresh: Boolean, speed: Boolean, updateIfSpeed: Boolean, cacheIfError: Boolean) =
            getValue(null, fresh, speed, updateIfSpeed, cacheIfError)


    override fun update() {
        update(null)
    }

    override suspend fun setDataStr(newStrData: String, tmsp: Long?) {
        setDataStr(null, newStrData, tmsp)
    }

    override suspend fun setData(newData: D, tmsp: Long?) {
        setData(null, newData, tmsp)
    }

}

//class DataManager<D : Any>(
//        private val key: String,
//        private val cacheValidity: Long,
//        private val serializer: KSerializer<D>,
//        private val cache: Persistor,
//        private val onNewData: ((newData: D) -> Unit)? = null,
//        private val getFreshStrData: suspend () -> String
//) {
//
//    val updatePoker = MutablePoker()
//
//    private var _value: DatedData<D>? = null
//
//    private fun DatedData<*>.isValid() = (timestamp + cacheValidity) > currentTimeMillis()
//
//    suspend fun getValue(fresh: Boolean = false, speed: Boolean = false, updateIfSpeed: Boolean = true, cacheIfError: Boolean = true): D {
//        if (fresh) {
//            return getFreshData()
//        } else {
//            val currenValue = _value
//            if (currenValue?.isValid() == true) {
//                return currenValue.data
//            } else {
//                val cachedStr = cache.getString(key)
//                if (cachedStr != null) {
//                    val isCachedStrValid = cachedStr.isValid()
//                    if (speed || isCachedStrValid) {
//                        val cachedData: D? =
//                                try {
//                                    Json.nonstrict.parse(serializer, cachedStr.data)
//                                } catch (ex: Exception) {
//                                    SKLog.e("Problème au parse de la donnée $key en cache, le format a probablement changé", ex)
//                                    null
//                                }
//
//                        if (cachedData != null) {
//                            _value = DatedData(cachedData, cachedStr.timestamp)
//                            if (speed && !isCachedStrValid && updateIfSpeed) {
//                                update()
//                            }
//                            return cachedData
//                        } else {
//                            return getFreshData(if (cacheIfError) cachedStr.data else null)
//                        }
//                    } else {
//                        return getFreshData(if (cacheIfError) cachedStr.data else null)
//                    }
//                } else {
//                    return getFreshData()
//                }
//
//
//            }
//        }
//
//    }
//
//    fun update() {
//        CoroutineScope(Dispatchers.Main).launch {
//            getFreshData()
//        }
//    }
//
//    private val mutexRefresh = Mutex()
//
//    private suspend fun getFreshData(strCached: String? = null): D {
//        val refreshDemandTmsp = currentTimeMillis()
//        mutexRefresh.withLock {
//            val currentValue = _value
//            if (currentValue == null || (currentValue?.timestamp ?: 0) < refreshDemandTmsp) {
//                return try {
//                    val freshStrData = getFreshStrData()
//                    val freshData = Json.nonstrict.parse(serializer, freshStrData)
//                    onNewData?.invoke(freshData)
//                    val now = currentTimeMillis()
//                    cache.putString(key, freshStrData, now)
//                    _value = DatedData(freshData, now)
//                    updatePoker.poke()
//                    freshData
//                } catch (ex: Exception) {
//                    if (strCached != null) {
//                        try {
//                            Json.nonstrict.parse(serializer, strCached)
//                        } catch (exParseCache: Exception) {
//                            throw ex
//                        }
//                    } else {
//                        throw ex
//                    }
//                }
//            } else {
//                return currentValue.data
//            }
//        }
//    }
//
//    suspend fun setDataStr(newStrData: String, tmsp: Long? = null) {
//        val date = tmsp ?: currentTimeMillis()
//        val newData = Json.nonstrict.parse(serializer, newStrData)
//        cache.putString(key, newStrData, date)
//        _value = DatedData(newData, date)
//        onNewData?.invoke(newData)
//        updatePoker.poke()
//    }
//
//    suspend fun setData(newData: D, tmsp: Long? = null) {
//        val date = tmsp ?: currentTimeMillis()
//        _value = DatedData(newData, date)
//        cache.putString(key, Json.nonstrict.stringify(serializer, newData), date)
//        onNewData?.invoke(newData)
//        updatePoker.poke()
//    }
//
//}