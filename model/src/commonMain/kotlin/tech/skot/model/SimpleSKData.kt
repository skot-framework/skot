package tech.skot.model

import kotlinx.coroutines.flow.MutableStateFlow

abstract class SimpleSKData<D : Any> : SKData<D> {

    override val flow = MutableStateFlow<DatedData<D>?>(null)
    override val defaultValidity = 5 * 60 * 1000L
    override val _current
        get() = flow.value

    abstract suspend fun newDatedData(): DatedData<D>

    override suspend fun update(): D {
        val newDatedValue = newDatedData()
        flow.value = newDatedValue
        return newDatedValue.data
    }

}
