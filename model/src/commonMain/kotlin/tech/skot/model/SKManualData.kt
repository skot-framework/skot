package tech.skot.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import tech.skot.core.SKLog
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

open class SKManualDataWithCache<D : Any>(
    private val name: String,
    private val serializer: KSerializer<D>,
    private val key: String? = null,
    private val cache: SKPersistor = globalCache,
    private val initialDefaultValue: D
) : SKData<D> {
    override val flow: MutableStateFlow<DatedData<D>?> = MutableStateFlow(null)
    override val defaultValidity = Long.MAX_VALUE
    override val _current: DatedData<D>?
        get() = flow.value


    private val initMutex = Mutex()

    private suspend fun initWithCache() {
        initMutex.withLock {
            if (flow.value == null) {
                flow.value =
                    cache.getDataSecured(serializer, name, key)?.let { DatedData(it.data, it.timestamp) }
            }
        }
    }

    override suspend fun fallBackValue(): D? {
        if (flow.value == null) {
            initWithCache()
        }
        return _current?.data
    }

    override suspend fun get(validity: Long?): D {
        if (flow.value == null) {
            initWithCache()
        }
        return super.get(validity)
    }


    suspend fun setValue(newValue: D) {
        flow.value = DatedData(newValue)
        CoroutineScope(Dispatchers.Default).launch {
            cache.putDataSecured(
                serializer = serializer,
                name = name,
                data = newValue,
                key = key
            )
        }


    }

    override suspend fun update(): D {
        if (flow.value == null) {
            initWithCache()
        }
        return _current?.data ?: initialDefaultValue
    }


}



