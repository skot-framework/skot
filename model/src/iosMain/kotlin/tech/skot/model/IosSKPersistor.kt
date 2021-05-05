package tech.skot.model

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import tech.skot.model.persist.PersistDb

class IosSKPersistor(dbFilename: String): CommonSKPersistor() {
    override val db = PersistDb(
            NativeSqliteDriver(
                    PersistDb.Schema,
                    "$dbFilename.db"
            ).apply { PersistDb.Schema.create(this) }
    )
}