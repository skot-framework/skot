package tech.skot.model

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import tech.skot.model.persist.PersistDb

class JvmSKPersistor(dbFilename: String) : CommonSKPersistor() {
    override val db = PersistDb(
        JdbcSqliteDriver(
            JdbcSqliteDriver.IN_MEMORY
        ).apply { PersistDb.Schema.create(this) }
    )
}