package tech.skot.model

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import tech.skot.model.persist.PersistDb

class AndroidSKPersistor(context: Context, dbFilename: String) : CommonSKPersistor() {

    override val db = PersistDb(
        AndroidSqliteDriver(
            PersistDb.Schema,
            context,
            "$dbFilename.db"
        ).apply { PersistDb.Schema.create(this) }
    )
}