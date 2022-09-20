package tech.skot.core.test

import kotlinx.coroutines.flow.Flow
import tech.skot.core.SKLog
import tech.skot.model.DatedData
import tech.skot.model.SKData
import tech.skot.model.SKManualData

class SKDataMock<D : Any?>(val name: String) : SKData<D> {

    private var internalManual: SKManualData<D>? = null

    var error: Exception? = null

    fun setValue(newVal: D) {
        val currentInternal = internalManual
        if (currentInternal == null) {
            internalManual = SKManualData<D>(newVal)
        } else {
            currentInternal.value = newVal
        }
    }

    private val errorNotSetMessage: Exception by lazy {
        Exception("You have to set a value for SKData $name before it is watched")
    }

    override val flow: Flow<DatedData<D>?>
        get() = internalManual?.flow ?: throw errorNotSetMessage
    override val defaultValidity: Long
        get() = internalManual?.defaultValidity ?: throw errorNotSetMessage
    override val _current: DatedData<D>?
        get() {
            val currentInternalManual = internalManual
            return error?.let { throw it } ?: if (currentInternalManual == null) {
                throw errorNotSetMessage
            } else {
                currentInternalManual._current
            }
        }

    override suspend fun update(): D {
        val currentInternalManual = internalManual
        return error?.let { throw it } ?: if (currentInternalManual == null) {
            throw errorNotSetMessage
        } else {
            currentInternalManual.update()
        }
    }

    override suspend fun fallBackValue(): D? {
        val currentInternalManual = internalManual
        return error?.let { throw it } ?: if (currentInternalManual == null) {
            throw errorNotSetMessage
        } else {
            currentInternalManual.fallBackValue()
        }
    }
}