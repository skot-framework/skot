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

    suspend fun putString(
        name: String,
        data: String,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    //On n'utilise pas putString pour inclure la sérialisation dans le withContext
    suspend fun <D : Any> putData(
        serializer: KSerializer<D>,
        name: String,
        data: D,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    suspend fun getDate(
        name: String,
        key: String? = null
    ): Long?

    suspend fun getString(
        name: String,
        key: String? = null
    ): String?

    suspend fun <D : Any> getData(
        serializer: KSerializer<D>,
        name: String,
        key: String? = null
    ): D?


    suspend fun remove(name: String)
    suspend fun clear()


//    suspend fun <D : Any> putDatedData(
//        serializer: KSerializer<D>,
//        name: String,
//        datedData: DatedData<D>,
//        key: String? = null
//    ) {
//        putData(
//            serializer = serializer,
//            name = name,
//            data = datedData.data,
//            timestamp = datedData.timestamp,
//            key = key
//        )
//    }

//    suspend fun <D : Any> getDataSecured(
//        serializer: KSerializer<D>,
//        name: String,
//        key: String? = null
//    ): DatedData<D>? = try {
//        getData(serializer, name, key)
//    } catch (ex: Exception) {
//        SKLog.e(ex, "SKPersistor : Error getting data $name from cache")
//        null
//    }

//    suspend fun <D : Any> putDataSecured(serializer: KSerializer<D>, name: String, data: D, key: String? = null, timestamp: Long = currentTimeMillis()) {
//        try {
//            putData(serializer, name, data, key, timestamp)
//        }
//        catch (ex:Exception) {
//            SKLog.e(ex, "SKPersistor : Error putting data $name in cache")
//        }
//    }


//    suspend fun putStringSecured( name: String, data: String, key: String? = null, timestamp: Long = currentTimeMillis()) {
//        try {
//            putString(name, data, key, timestamp)
//        }
//        catch (ex:Exception) {
//            SKLog.e(ex, "SKPersistor : Error putting data $name in cache")
//        }
//    }


//    suspend fun getStringValue(name: String, key: String? = null): String?
//
//    suspend fun getStringSecured(name: String, key: String? = null): DatedData<String>? = try {
//        getString(name)
//    } catch (ex: Exception) {
//        null
//    }


}

//On fait le choix de tout sécuriser, les erreurs étant loggées, cela correspond aux cas d'usage standard
//à voir pour faire des versions non sécurisées des méthodes si besoin

abstract class CommonSKPersistor : SKPersistor {
    protected abstract val db: PersistDb

    override suspend fun <D : Any> putData(
        serializer: KSerializer<D>,
        name: String,
        data: D,
        key: String?,
        timestamp: Long
    ) {
        withContext(Dispatchers.Default) {
            _putString(name, Json.encodeToString(serializer, data), key, timestamp)
        }
    }

    override suspend fun putString(name: String, data: String, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data, key, timestamp)
        }
    }

    private fun _putString(name: String, data: String, key: String?, timestamp: Long) {
        try {
            db.persistedQueries.putByName(name, key, timestamp, data)
        } catch (ex: Exception) {
            SKLog.e(
                ex,
                "SKPersistor: Problem putting data with name: $name and key: $key"
            )
        }
    }

    override suspend fun getDate(name: String, key: String?): Long? {
        return withContext(Dispatchers.Default) {
            try {
                db.persistedQueries.obtainDateByName(name, key).executeAsOneOrNull()
            } catch (ex: Exception) {
                SKLog.e(
                    ex,
                    "SKPersistor: Problem getting date of data with name: $name and key: $key"
                )
                null
            }
        }
    }

    override suspend fun getString(name: String, key: String?): String? {
        return withContext(Dispatchers.Default) {
            try {
                _getString(name, key)
            } catch (ex: Exception) {
                SKLog.e(
                    ex,
                    "SKPersistor: Problem getting value of data with name: $name and key: $key"
                )
                null
            }
        }
    }

    fun _getString(name: String, key: String?) =
        try {
            db.persistedQueries.obtainValueByName(name, key).executeAsOneOrNull()
        } catch (ex: Exception) {
            var from = 0
            val step = 333333
            var newText: String? = null
            val stringBuilder = StringBuilder()

            db.persistedQueries.transaction {
                do {
                    newText =
                        db.persistedQueries.obtainPartOfValueByName(
                            from.toString(),
                            step.toString(),
                            name,
                            key
                        )
                            .executeAsOneOrNull()
                    stringBuilder.append(newText)
                    from += step

                } while (newText != null && newText != "")
            }

            stringBuilder.toString()
        }


    override suspend fun <D : Any> getData(
        serializer: KSerializer<D>,
        name: String,
        key: String?
    ): D? {
        return withContext(Dispatchers.Default) {
            try {
                _getString(name, key)?.let {
                    Json.decodeFromString(serializer, it)
                }
            } catch (ex: Exception) {
                SKLog.e(
                    ex,
                    "SKPersistor: Problem getting value of data with name: $name and key: $key"
                )
                null
            }
        }
    }


    override suspend fun remove(name: String) {
        withContext(Dispatchers.Default) {
            try {
                db.persistedQueries.remove(name)
                db.persistedQueries.vacuum()
            } catch (ex: Exception) {
                SKLog.e(
                    ex,
                    "SKPersistor: Problem removing data with name: $name"
                )
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.Default) {
            try {
                db.persistedQueries.clear()
                db.persistedQueries.vacuum()
            } catch (ex: Exception) {
                SKLog.e(
                    ex,
                    "SKPersistor: Problem clearing"
                )
            }
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