package tech.skot.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import tech.skot.core.SKLog
import tech.skot.core.currentTimeMillis
import tech.skot.core.di.get
import tech.skot.model.persist.PersistDb

data class DatedDataWithKey<D : Any>(val data: D, val id: String?, val timestamp: Long)


//On fait le choix de tout sécuriser, les erreurs étant loggées, cela correspond aux cas d'usage standard
//à voir pour faire des versions non sécurisées des méthodes si besoin de différencier le cas “pas de donnée” du cas “erreur de lecture”

interface SKPersistor {

    suspend fun putString(
        name: String,
        data: String,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    suspend fun putInt(
        name: String,
        data: Int,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    suspend fun putFloat(
        name: String,
        data: Float,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    suspend fun putDouble(
        name: String,
        data: Float,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    suspend fun putLong(
        name: String,
        data: Boolean,
        key: String? = null,
        timestamp: Long = currentTimeMillis()
    )

    suspend fun putBoolean(
        name: String,
        data: Boolean,
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

    suspend fun getDateOfData(
        name: String,
        key: String? = null
    ): Long?

    suspend fun getString(
        name: String,
        key: String? = null
    ): String?

    suspend fun getInt(
        name: String,
        key: String? = null
    ): Int?

    suspend fun getBoolean(
        name: String,
        key: String? = null
    ): Boolean?

    suspend fun getLong(
        name: String,
        key: String? = null
    ): Long?

    suspend fun getFloat(
        name: String,
        key: String? = null
    ): Float?

    suspend fun getDouble(
        name: String,
        key: String? = null
    ): Double?

    suspend fun <D : Any> getData(
        serializer: KSerializer<D>,
        name: String,
        key: String? = null
    ): D?


    suspend fun remove(name: String)
    suspend fun clear()

}


abstract class CommonSKPersistor : SKPersistor {
    protected abstract val db: PersistDb

    val json: Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }


    override suspend fun <D : Any> putData(
        serializer: KSerializer<D>,
        name: String,
        data: D,
        key: String?,
        timestamp: Long
    ) {
        withContext(Dispatchers.Default) {
            _putString(name,json.encodeToString(serializer, data) , key, timestamp)
        }
    }

    override suspend fun putString(name: String, data: String, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data, key, timestamp)
        }
    }

    override suspend fun putInt(name: String, data: Int, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data.toString(), key, timestamp)
        }
    }


    override suspend fun putFloat(name: String, data: Float, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data.toString(), key, timestamp)
        }
    }


    override suspend fun putDouble(name: String, data: Float, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data.toString(), key, timestamp)
        }
    }

    override suspend fun putLong(name: String, data: Boolean, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data.toString(), key, timestamp)
        }
    }

    override suspend fun putBoolean(name: String, data: Boolean, key: String?, timestamp: Long) {
        withContext(Dispatchers.Default) {
            _putString(name, data.toString(), key, timestamp)
        }
    }

    private fun _putString(name: String, data: String, key: String?, timestamp: Long) {
        try {
            db.persistedQueries.putByName(name, key, timestamp, data)
        } catch (ex: Exception) {
            SKLog.e(
                ex,
                "SKPersistor: Problem putting Data with name: $name and key: $key"
            )
        }
    }


    override suspend fun getDateOfData(name: String, key: String?): Long? {
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

    override suspend fun getInt(name: String, key: String?): Int? {
        return getData(Int.serializer(), name, key)
    }

    override suspend fun getBoolean(name: String, key: String?): Boolean? {
        return getData(Boolean.serializer(), name, key)
    }

    override suspend fun getLong(name: String, key: String?): Long? {
        return getData(Long.serializer(),name, key)
    }

    override suspend fun getFloat(name: String, key: String?): Float? {
        return getData(Float.serializer(),name, key)
    }

    override suspend fun getDouble(name: String, key: String?): Double? {
        return getData(Double.serializer(),name, key)
    }

    fun _getString(name: String, key: String?) =
        try {
            db.persistedQueries.obtainValueByName(name, key).executeAsOneOrNull()
        } catch (ex: Exception) {
            var from = 0
            val step = 333333
            var newText: String?
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
                    json.decodeFromString(serializer, it)
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