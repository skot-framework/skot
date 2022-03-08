package tech.skot.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tech.skot.core.SKLog

abstract class SimpleSKData<D : Any?> : SKData<D> {

    override val flow = MutableStateFlow<DatedData<D>?>(null)
    override val defaultValidity = 5 * 60 * 1000L
    override val _current
        get() = flow.value

    abstract suspend fun newDatedData(): DatedData<D>

    private val updateMutex = Mutex()

    override suspend fun update(): D {
        val refreshDemandTmsp = currentTimeMillis()
        return updateMutex.withLock {
            val currentDatedData = _current
            if (currentDatedData != null && currentDatedData.timestamp > refreshDemandTmsp) {
                currentDatedData.data
            } else {
                val newDatedValue = newDatedData()
                flow.value = newDatedValue
                return newDatedValue.data
            }
        }
    }

    fun setData(newManualData: D) {
        setDatedData(DatedData(newManualData, currentTimeMillis()))
    }

    open fun setDatedData(newDatedData: DatedData<D>) {
        flow.value = newDatedData
    }

    override suspend fun fallBackValue(): D? = flow.value?.data

}

suspend fun <D : Any?> SKData<D>.getWithFallBackIfError(validity: Long? = null): D {
    return try {
        get(validity)
    } catch (ex: Exception) {
        SKLog.e(ex, "get fail will get fallbackvalue")
        fallBackValue() ?: throw ex
    }
}