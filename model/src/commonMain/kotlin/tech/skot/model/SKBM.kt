package tech.skot.model

import kotlinx.serialization.KSerializer

abstract class SKBM(val key:String?) {


    open inner class DistantData<D:Any>(
        name:String,
        serializer: KSerializer<D>,
        cache: SKPersistor = globalCache,
        validity:Long? = null,
        fetchData: suspend () -> D
    ):SKDistantDataWithCache<D>(
        name = name,
        serializer = serializer,
        key = key,
        cache = cache,
        validity = validity,
        fetchData = fetchData
    )

    open inner class ManualData<D:Any>(
        name: String,
        serializer: KSerializer<D>,
        cache: SKPersistor = globalCache,
        initialDefaultValue: D
    ):SKManualDataWithCache<D>(
        name = name,
        serializer = serializer,
        key = key,
        cache = cache,
        initialDefaultValue = initialDefaultValue
    )
}