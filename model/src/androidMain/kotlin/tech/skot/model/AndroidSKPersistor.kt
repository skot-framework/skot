package tech.skot.model

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import tech.skot.model.persist.PersistDb

class AndroidSKPersistor(context: Context, dbFilename: String, cache:Boolean = false) : CommonSKPersistor() {

    private val dbName = if (cache) {
        context.cacheDir.resolve("$dbFilename.db").absolutePath
    } else  {
        "$dbFilename.db"
    }
    override val db = PersistDb(
        AndroidSqliteDriver(
            PersistDb.Schema,
            context,
            dbName
        ).apply { PersistDb.Schema.create(this) }
    )
}