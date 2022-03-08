package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import tech.skot.core.SKLog

open class SKDistantData<D : Any?>(validity: Long? = null, private val fetchData: suspend () -> D) :
    SimpleSKData<D>() {
    override suspend fun newDatedData() = DatedData(fetchData(), currentTimeMillis())
    override val defaultValidity = validity ?: super.defaultValidity
}

