package tech.skot.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlinx.coroutines.suspendCancellableCoroutine
import tech.skot.model.persist.PersistDb

class IosPersistor(dbFilename: String): CommonPersistor(dbFilename) {
    override val db = PersistDb(
            NativeSqliteDriver(
                    PersistDb.Schema,
                    "$dbFilename.db"
            ).apply { PersistDb.Schema.create(this) }
    )
}

//class IosPersistor(dbFilename: String) : Persistor {
////    override val db = PersistDb(
////            NativeSqliteDriver(
////                    PersistDb.Schema,
////                    "$dbFilename.db"
////            ).apply { PersistDb.Schema.create(this) }
////    )
//
//    override suspend fun <D : Any> putData(serializer: KSerializer<D>, key: String, id: String?, data: D, timestamp: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun <D : Any> getData(serializer: KSerializer<D>, key: String): DatedData<D>? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun putString(key: String, id: String?, data: String, timestamp: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getString(key: String): DatedData<String>? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun remove(key: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun clear() {
//        TODO("Not yet implemented")
//    }
//}