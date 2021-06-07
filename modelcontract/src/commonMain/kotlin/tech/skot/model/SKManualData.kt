package tech.skot.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty

open class SKManualData<D : Any?>(initialValue: D, private val onChange:(()->Unit)? = null) : SKData<D> {
    override val flow = MutableStateFlow(DatedData(initialValue, currentTimeMillis()))
    override val defaultValidity = Long.MAX_VALUE
    override val _current: DatedData<D>
        get() = flow.value

    var value: D
        get() = _current.data
        set(newValue) {
            val oldValue = flow.value
            flow.value = DatedData(newValue)
            onChange?.let {
                if (oldValue != newValue) {
                    it.invoke()
                }
            }
        }

    override suspend fun update(): D {
        return _current.data
    }

    override suspend fun fallBackValue(): D? = _current.data

    operator fun setValue(thisObj: Any?, property: KProperty<*>, value: D) {
        this.value = value
    }

    operator fun getValue(thisObj: Any?, property: KProperty<*>) = this.value
}