package tech.skot.core.test

import kotlinx.coroutines.flow.Flow
import tech.skot.model.DatedData
import tech.skot.model.SKData
import tech.skot.model.SKManualData
import tech.skot.model.SKPaginatedData

class SKPaginatedDataMock<D:Any>(val name:String): SKPaginatedData<D> {

    private var internalManual:SKManualData<List<D>>? = null

    var error:Exception? = null

    fun setValue(newVal:List<D>) {
        val currentInternal = internalManual
        if (currentInternal == null) {
            internalManual = SKManualData(newVal)
        }
        else {
            currentInternal.value = newVal
        }
    }

    private val errorNotSetMessage: Exception by lazy {
        Exception("You have to set a value for SKData $name before it is watched")
    }


    var nbPagesLoaded = 0

    override var mayHaveAnotherPage = true

    override suspend fun oneMorePage() {
        nbPagesLoaded++
    }
    override val flow: Flow<DatedData<List<D>>?>
        get() = internalManual?.flow ?: throw errorNotSetMessage
    override val defaultValidity: Long
        get() = internalManual?.defaultValidity ?: throw errorNotSetMessage
    override val _current: DatedData<List<D>>?
        get() =error?.let { throw it } ?:  internalManual?._current ?: throw errorNotSetMessage

    override suspend fun update(): List<D> {
        nbPagesLoaded = 1
        return error?.let { throw it } ?: internalManual?.update() ?: throw errorNotSetMessage
    }

    override suspend fun fallBackValue(): List<D>? {
        return error?.let { throw it } ?: internalManual?.fallBackValue() ?: throw errorNotSetMessage
    }
}