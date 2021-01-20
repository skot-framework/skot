package tech.skot.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import tech.skot.core.currentTimeMillis
import tech.skot.core.di.get
import tech.skot.model.persist.PersistDb

interface Persistor {
    suspend fun <D : Any> putData(serializer: KSerializer<D>, key: String, id:String?, data: D, timestamp: Long = currentTimeMillis())
    suspend fun <D : Any> putData(serializer: KSerializer<D>, key: String, data: D, timestamp: Long = currentTimeMillis()) {
        putData(serializer, key, null, data)
    }

    suspend fun <D : Any> getData(serializer: KSerializer<D>, key: String): DatedData<D>?
    suspend fun <D : Any> getDataSecured(serializer: KSerializer<D>, key: String) = try {
        getData(serializer, key)
    }
    catch (ex:Exception) {
        null
    }
    suspend fun putString(key:String, id:String?, data:String, timestamp: Long = currentTimeMillis())
    suspend fun putString(key:String, data:String, timestamp: Long = currentTimeMillis()) {
        putString(key, null, data, timestamp)
    }
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


abstract class CommonPersistor(dbFilename: String) : Persistor {
    abstract protected val db: PersistDb

    override suspend fun <D : Any> putData(
            serializer: KSerializer<D>,
            key: String,
            id: String?,
            data: D,
            timestamp: Long
    ) {
        return withContext(Dispatchers.Default) {
            db.persistedQueries.putByKey(key, id, timestamp, Json.encodeToString(serializer, data))
        }

    }

    override suspend fun <D : Any> getData(
            serializer: KSerializer<D>,
            key: String
    ): DatedData<D>? {
        return withContext(Dispatchers.Default) {
            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
                DatedData(Json.decodeFromString(serializer, it.data), it.id, it.timestamp)
            }
        }
    }

    override suspend fun putString(key: String, id:String?, data: String, timestamp: Long) {
        return withContext(Dispatchers.Default) {
            db.persistedQueries.putByKey(key, id, timestamp, data)
        }
    }

    override suspend fun getString(key: String): DatedData<String>? {
        return withContext(Dispatchers.Default) {
            delay(5000)
            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
                DatedData(it.data, it.id, it.timestamp)
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.Default) {
            db.persistedQueries.clear()
            db.persistedQueries.vacuum()
        }
    }

    override suspend fun remove(key: String) {
        withContext(Dispatchers.Default) {
            db.persistedQueries.remove(key)
            db.persistedQueries.vacuum()
        }
    }

}


@InternalSerializationApi
suspend inline fun <reified D : Any> Persistor.putData(key: String, id:String?, data: D, timestamp: Long) {
    putData(D::class.serializer(), key, id, data, timestamp)
}

@InternalSerializationApi
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