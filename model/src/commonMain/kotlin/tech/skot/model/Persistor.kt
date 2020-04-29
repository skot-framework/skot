package tech.skot.model

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import tech.skot.core.di.get

interface Persistor {
    suspend fun <D : Any> putData(serializer: KSerializer<D>, key: String, data: D, timestamp: Long = currentTimeMillis())
    suspend fun <D : Any> getData(serializer: KSerializer<D>, key: String): DatedData<D>?
    suspend fun <D : Any> getDataSecured(serializer: KSerializer<D>, key: String) = try {
        getData(serializer, key)
    }
    catch (ex:Exception) {
        null
    }
    suspend fun putString(key:String, data:String, timestamp: Long = currentTimeMillis())
    suspend fun getString(key:String): DatedData<String>?
    suspend fun getStringSecured(key:String) = try {
        getString(key)
    }
    catch (ex:Exception) {
        null
    }

    suspend fun remove(key:String)
    suspend fun clear()
}

@ImplicitReflectionSerializer
suspend inline fun <reified D : Any> Persistor.putData(key: String, data: D, timestamp: Long) {
    putData(D::class.serializer(), key, data, timestamp)
}

@ImplicitReflectionSerializer
suspend inline fun <reified D : Any> Persistor.getData(key: String): DatedData<D>? =
        getData(D::class.serializer(), key)


interface PersistorFactory {
    fun getPersistor(dbFileName: String):Persistor
}

val globalCache by lazy {
    get<PersistorFactory>().getPersistor("global")
}

val userCache by lazy {
    get<PersistorFactory>().getPersistor("user")
}