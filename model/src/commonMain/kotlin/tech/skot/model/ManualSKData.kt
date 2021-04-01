package tech.skot.model

import kotlinx.coroutines.flow.MutableStateFlow

class ManualSKData<D : Any>(initialValue: D) : SKData<D> {
    override val flow = MutableStateFlow<DatedData<D>?>(DatedData(initialValue, currentTimeMillis()))
    override val defaultValidity = Long.MAX_VALUE
    override val _current
        get() = flow.value

    var value: D
        get() = _current!!.data
        set(newValue) {
            flow.value = DatedData(newValue)
        }

    override suspend fun update(): D {
        return _current!!.data
    }

}