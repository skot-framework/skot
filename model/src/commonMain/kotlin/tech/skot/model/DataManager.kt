package tech.skot.model

import kotlinx.serialization.KSerializer
import tech.skot.contract.modelcontract.Poker


interface DataManager<D : Any> {
    val updatePoker: Poker
    suspend fun getValue(fresh: Boolean = false, speed: Boolean = false, updateIfSpeed: Boolean = true, cacheIfError: Boolean = true): D
    fun update()

    suspend fun setDataStr(newStrData: String, tmsp: Long? = null)
    suspend fun setData(newData: D, tmsp: Long? = null)

    suspend fun invalidate()
}

class DataManagerImpl<D : Any>(
        key: String,
        cacheValidity: Long?,
        serializer: KSerializer<D>,
        cache: Persistor,
        onNewData: ((newData: D) -> Unit)? = null,
        getFreshStrData: suspend (id: String?) -> String
) : UnivDataManagerImpl<D>(key, cacheValidity, serializer, cache, onNewData, getFreshStrData), DataManager<D> {
    override suspend fun getValue(fresh: Boolean, speed: Boolean, updateIfSpeed: Boolean, cacheIfError: Boolean) =
            univGetValue(null, fresh, speed, updateIfSpeed, cacheIfError)


    override fun update() {
        univUpdate(null)
    }

    override suspend fun setDataStr(newStrData: String, tmsp: Long?) {
        univSetDataStr(null, newStrData, tmsp)
    }

    override suspend fun setData(newData: D, tmsp: Long?) {
        univSetData(null, newData, tmsp)
    }

}