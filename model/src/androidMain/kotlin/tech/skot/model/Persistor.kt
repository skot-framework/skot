package tech.skot.model

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.skot.model.persist.PersistDb

class AndroidPersistor(context: Context, dbFilename: String) : Persistor {
    val db = PersistDb(
            AndroidSqliteDriver(
                    PersistDb.Schema,
                    context,
                    "$dbFilename.db"
            ).apply { PersistDb.Schema.create(this) }
    )

    override suspend fun <D : Any> putData(
            serializer: KSerializer<D>,
            key: String,
            data: D,
            timestamp: Long
    ) {
        return withContext(Dispatchers.IO) {
            db.persistedQueries.putByKey(key, timestamp, Json.stringify(serializer, data))
        }

    }

    override suspend fun <D : Any> getData(
            serializer: KSerializer<D>,
            key: String
    ): PersistedData<D>? {
        return withContext(Dispatchers.IO) {
            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
                PersistedData(Json.parse(serializer, it.data), it.timestamp)
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            db.persistedQueries.clear()
            db.persistedQueries.vacuum()
        }
    }


}