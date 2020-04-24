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
        cacheValidity: Long?,
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