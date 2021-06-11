package tech.skot.model

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlin.math.min

interface SKData<D : Any?> {


    val flow: Flow<DatedData<D>?>
    val defaultValidity: Long
    val _current: DatedData<D>?

    suspend fun update(): D
    suspend fun fallBackValue(): D?

    open suspend fun get(validity: Long? = null): D {
        val datedCurrentValue = _current
        val usedValidity = validity ?: defaultValidity
        return if (datedCurrentValue == null || usedValidity == 0L || ((datedCurrentValue.timestamp + usedValidity) > 0 && currentTimeMillis() > (datedCurrentValue.timestamp + usedValidity))) {
            update()
        } else {
            datedCurrentValue.data
        }
    }

    suspend fun getDirect() = _current?.data ?: get(validity = Long.MAX_VALUE)

}

fun <D : Any?, O : Any?> SKData<D>.map(transform: (d: D) -> O): SKData<O> {
    return object : SKData<O> {
        override val defaultValidity = this@map.defaultValidity
        override val flow = this@map.flow.map {
            it?.let { transformDatedData(it) }
        }
        override val _current
            get() = this@map._current?.let { transformDatedData(it) }

        private fun transformDatedData(datedData: DatedData<D>) =
            transform(datedData.data).let { transformedValue ->
                DatedData(
                    data = transformedValue,
                    timestamp = datedData.timestamp
                )
            }

        override suspend fun update(): O {
            return transform(this@map.update())
        }

        override suspend fun get(validity: Long?): O {
            return transform(this@map.get(validity))
        }

        override suspend fun fallBackValue(): O? {
            return this@map.fallBackValue()?.let(transform)
        }
    }
}

fun <D1 : Any?, D2 : Any?> SKData<D1>.combine(other: SKData<D2>) = combineSKData(this, other)

fun <D1 : Any?, D2 : Any?> combineSKData(
    data1: SKData<D1>,
    data2: SKData<D2>
): SKData<Pair<D1, D2>> {
    return object : SKData<Pair<D1, D2>> {

        override val defaultValidity = min(data1.defaultValidity, data2.defaultValidity)
        override val _current: DatedData<Pair<D1, D2>>?
            get() = buildPair(data1._current, data2._current)


        private fun buildPair(
            datedData1: DatedData<D1>?,
            datedData2: DatedData<D2>?
        ): DatedData<Pair<D1, D2>>? =
            if (datedData1 != null && datedData2 != null) {
                DatedData(
                    data = Pair(datedData1.data, datedData2.data),
                    timestamp = min(datedData1.timestamp, datedData2.timestamp)
                )
            } else {
                null
            }

        override val flow: Flow<DatedData<Pair<D1, D2>>?> = combineTransform(
            data1.flow,
            data2.flow
        ) { datedDataFlow1, datedDataFlow2 ->
            buildPair(datedDataFlow1, datedDataFlow2)?.let { emit(it) }

        }

        override suspend fun update(): Pair<D1, D2> {
            return coroutineScope {
                val updatedData1 =
                    async {
                        data1.update()
                    }
                val updatedData2 =
                    async {
                        data2.update()
                    }
                Pair(updatedData1.await(), updatedData2.await())
            }
        }

        override suspend fun get(validity: Long?): Pair<D1, D2> {
            return coroutineScope {
                val gettedData1 = async {
                    data1.get(validity)
                }

                val gettedData2 = async {
                    data2.get(validity)
                }
                Pair(gettedData1.await(), gettedData2.await())
            }
        }

        override suspend fun fallBackValue(): Pair<D1, D2>? {
            val data1FallBackValue = data1.fallBackValue()
            val data2FallBackValue = data2.fallBackValue()
            return if (data1FallBackValue != null && data2FallBackValue != null) {
                Pair(data1FallBackValue, data2FallBackValue)
            } else {
                null
            }
        }
    }

}