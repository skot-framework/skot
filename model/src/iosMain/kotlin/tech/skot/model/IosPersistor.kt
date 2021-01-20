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