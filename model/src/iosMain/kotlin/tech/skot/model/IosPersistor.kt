package tech.skot.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlinx.coroutines.suspendCancellableCoroutine
import tech.skot.model.persist.PersistDb
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.*

class IosPersistor(dbFilename: String) : Persistor {
    private val db = PersistDb(
            NativeSqliteDriver(
                    PersistDb.Schema,
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
//        suspendCancellableCoroutine<Unit> {
////            DispatchQueue.glo
//            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
//                db.persistedQueries.putByKey(key, id, timestamp, Json.encodeToString(serializer, data))
//            })
////            dispatch_async(dispatch_get_main_queue()){toRun()}
//        }
//        return withContext(Dispatchers.IO) {
//            db.persistedQueries.putByKey(key, id, timestamp, Json.encodeToString(serializer, data))
//        }

    }

    override suspend fun <D : Any> getData(
            serializer: KSerializer<D>,
            key: String
    ): DatedData<D>? {
//        return withContext(Dispatchers.IO) {
//            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
//                DatedData(Json.decodeFromString(serializer, it.data), it.id, it.timestamp)
//            }
//        }
        return null
    }

    override suspend fun putString(key: String, id:String?, data: String, timestamp: Long) {
//        suspend {
//            db.persistedQueries.putByKey(key, id, timestamp, data)
//        }
//        return withContext(Dispatchers.IO) {
//
//        }


        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0), {
            db.persistedQueries.putByKey(key, id, timestamp, data)
        })
    }

    override suspend fun getString(key: String): DatedData<String>? {
//        return withContext(Dispatchers.IO) {
//            db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
//                DatedData(it.data, it.id, it.timestamp)
//            }
//        }

        return db.persistedQueries.obtainByKey(key).executeAsOneOrNull()?.let {
                DatedData(it.data, it.id, it.timestamp)
            }

//        return null
    }

    override suspend fun clear() {
//        withContext(Dispatchers.IO) {
//            db.persistedQueries.clear()
//            db.persistedQueries.vacuum()
//        }
    }

    override suspend fun remove(key: String) {
//        withContext(Dispatchers.IO) {
//            db.persistedQueries.remove(key)
//            db.persistedQueries.vacuum()
//        }
    }

}