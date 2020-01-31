package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.skot.contract.modelcontract.Poker
import tech.skot.core.SKLog

class DataManager<D : Any>(
        private val key: String,
        private val cacheValidity: Long,
        private val serializer: KSerializer<D>,
        private val cache: Persistor,
        private val getFreshStrData: suspend () -> String
) {

    val updatePoker = Poker()

    private var _value: DatedData<D>? = null

    private fun DatedData<*>.isValid() = (timestamp + cacheValidity) > currentTimeMillis()

    suspend fun getValue(fresh: Boolean = false, speed: Boolean = false, updateIfSpeed: Boolean = true): D {
        if (fresh) {
            return getFreshData()
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
                                    Json.nonstrict.parse(serializer, cachedStr.data)
                                } catch (ex: Exception) {
                                    SKLog.e("Problème au parse de la donnée $key en cache, le format a probablement changé", ex)
                                    null
                                }

                        if (cachedData != null) {
                            _value = DatedData(cachedData, cachedStr.timestamp)
                            if (speed && !isCachedStrValid && updateIfSpeed) {
                                update()
                            }
                            return cachedData
                        } else {
                            return getFreshData()
                        }
                    } else {
                        return getFreshData()
                    }
                } else {
                    return getFreshData()
                }


            }
        }

    }

    fun update() {
        CoroutineScope(Dispatchers.Main).launch {
            getFreshData()
        }
    }

    private suspend fun getFreshData(): D {
        val freshStrData = getFreshStrData()
        val freshData = Json.nonstrict.parse(serializer, freshStrData)
        val now = currentTimeMillis()
        cache.putString(key, freshStrData, now)
        _value = DatedData(freshData, now)
        updatePoker.poke()
        return freshData
    }

    suspend fun setDataStr(newStrData: String, tmsp: Long? = null) {
        val date = tmsp ?: currentTimeMillis()
        val newData = Json.nonstrict.parse(serializer, newStrData)
        cache.putString(key, newStrData, date)
        _value = DatedData(newData, date)
        updatePoker.poke()
    }

    suspend fun setData(newData: D, tmsp: Long? = null) {
        val date = tmsp ?: currentTimeMillis()
        _value = DatedData(newData, date)
        cache.putString(key, Json.nonstrict.stringify(serializer, newData), date)
        updatePoker.poke()
    }

}