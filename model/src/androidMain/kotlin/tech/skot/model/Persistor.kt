package tech.skot.model

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.skot.model.persist.PersistDb

class AndroidPersistor(context: Context, dbFilename: String) : Persistor {
    private val db = PersistDb(
            AndroidSqliteDriver(
                    PersistDb.Schema,
                    context,
                    "$dbFilename.db"
            ).apply { PersistDb.Schema.create(this) }
    )

    override suspend fun <D : Any> putData(
            serializer: KSerializer<D>,
            key: String,
            id: String?,
            data: D,
            timestamp: Long
    ) {
        return withContext(Dispatchers.IO) {
            db.persistedQueries.putByKey(key, id, timestamp, Json.encodeToString(serializer, data))
        }

    }

    override suspend fun <D : Any> getData(
            serializer: KSerializer<D>,
            key: String
    ): DatedData<D>? {
        return withContext(Dispatchers.IO) {
            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
                DatedData(Json.decodeFromString(serializer, it.data_), it.id, it.timestamp)
            }
        }
    }

    override suspend fun putString(key: String, id:String?, data: String, timestamp: Long) {
        return withContext(Dispatchers.IO) {
            db.persistedQueries.putByKey(key, id, timestamp, data)
        }
    }

    override suspend fun getString(key: String): DatedData<String>? {
        return withContext(Dispatchers.IO) {
            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
                DatedData(it.data_, it.id, it.timestamp)
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            db.persistedQueries.clear()
            db.persistedQueries.vacuum()
        }
    }

    override suspend fun remove(key: String) {
        withContext(Dispatchers.IO) {
            db.persistedQueries.remove(key)
            db.persistedQueries.vacuum()
        }
    }

}