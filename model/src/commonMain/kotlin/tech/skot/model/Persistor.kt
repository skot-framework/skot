package tech.skot.model

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import tech.skot.core.di.get

data class PersistedData<D : Any>(val data: D, val timestamp: Long)

interface Persistor {
    suspend fun <D : Any> putData(serializer: KSerializer<D>, key: String, data: D, timestamp: Long)
    suspend fun <D : Any> getData(serializer: KSerializer<D>, key: String): PersistedData<D>?
    suspend fun clear()
}

@ImplicitReflectionSerializer
suspend inline fun <reified D : Any> Persistor.putData(key: String, data: D, timestamp: Long) {
    putData(D::class.serializer(), key, data, timestamp)
}

@ImplicitReflectionSerializer
suspend inline fun <reified D : Any> Persistor.getData(key: String): PersistedData<D>? =
        getData(D::class.serializer(), key)


interface PersistorFactory {
    fun getPersistor(dbFileName: String):Persistor
}

val globalCache by lazy {
    get<PersistorFactory>().getPersistor("global")
}

