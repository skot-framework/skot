package tech.skot.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.skot.core.SKLog
import tech.skot.core.currentTimeMillis
import tech.skot.core.di.get
import tech.skot.model.persist.PersistDb

data class DatedDataWithKey<D : Any>(val data: D, val id: String?, val timestamp: Long)

interface SKPersistor {

    //On n'utilise pas putString pour inclure la sérialisation dans le withContext
    suspend fun <D : Any> putData(serializer: KSerializer<D>, name: String, data: D, key: String? = null, timestamp: Long = currentTimeMillis())

    suspend fun <D : Any> putDatedData(serializer: KSerializer<D>, name: String, datedData: DatedData<D>, key: String? = null) {
        putData(
                serializer = serializer,
                name = name,
                data = datedData.data,
                timestamp = datedData.timestamp,
                key = key)
    }

    suspend fun <D : Any> getDataSecured(serializer: KSerializer<D>, name: String, key: String? = null) :DatedData<D>? = try {
        getData(serializer, name, key)
    } catch (ex: Exception) {
        SKLog.e(ex, "SKPersistor : Error getting data $name from cache")
        null
    }

    suspend fun <D : Any> putDataSecured(serializer: KSerializer<D>, name: String, data: D, key: String? = null, timestamp: Long = currentTimeMillis()) {
        try {
            putData(serializer, name, data, key, timestamp)
        }
        catch (ex:Exception) {
            SKLog.e(ex, "SKPersistor : Error putting data $name in cache")
        }
    }


    suspend fun <D : Any> getData(serializer: KSerializer<D>, name: String, key: String? = null): DatedData<D>?


    suspend fun putString(name: String, data: String, key: String? = null, timestamp: Long = currentTimeMillis())

    suspend fun putStringSecured( name: String, data: String, key: String? = null, timestamp: Long = currentTimeMillis()) {
        try {
            putString(name, data, key, timestamp)
        }
        catch (ex:Exception) {
            SKLog.e(ex, "SKPersistor : Error putting data $name in cache")
        }
    }

    suspend fun getString(name: String, key: String? = null): DatedData<String>?
    suspend fun getStringSecured(name: String, key: String? = null):DatedData<String>? = try {
        getString(name)
    } catch (ex: Exception) {
        null
    }

    suspend fun remove(name: String)
    suspend fun clear()
}


abstract class CommonSKPersistor : SKPersistor {
    protected abstract val db: PersistDb

    override suspend fun putString(name: String, data: String, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            db.persistedQueries.putByName(name, key, timestamp, data)
        }
    }

    override suspend fun <D : Any> putData(serializer: KSerializer<D>, name: String, data: D, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            db.persistedQueries.putByName(name, key, timestamp, Json.encodeToString(serializer, data))
        }
    }

    override suspend fun getString(name: String, key: String?): DatedData<String>? {
        return withContext(Dispatchers.Default) {
            db.persistedQueries.obtainByName(name).executeAsOneOrNull()?.let {
                if (it.key == key) {
                    DatedData(it.value, it.timestamp)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun <D : Any> getData(serializer: KSerializer<D>, name: String, key: String?): DatedData<D>? {
        return withContext(Dispatchers.Default) {
            db.persistedQueries.obtainByName(name).executeAsOneOrNull()?.let {
                if (it.key == key) {
                    DatedData(Json.decodeFromString(serializer, it.value), it.timestamp)
                } else {
                    null
                }
            }
        }
    }


    override suspend fun clear() {
        withContext(Dispatchers.Default) {
            db.persistedQueries.clear()
            db.persistedQueries.vacuum()
        }
    }

    override suspend fun remove(name: String) {
        withContext(Dispatchers.Default) {
            db.persistedQueries.remove(name)
            db.persistedQueries.vacuum()
        }
    }

}


interface PersistorFactory {
    fun getPersistor(dbFileName: String): SKPersistor
}

val globalCache by lazy {
    get<PersistorFactory>().getPersistor("global")
}

val userCache by lazy {
    get<PersistorFactory>().getPersistor("user")
}