package tech.skot.model

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.skot.model.persist.PersistDb

class AndroidPersistor(context: Context, dbFilename: String): CommonPersistor(dbFilename) {
    override val db = PersistDb(
            AndroidSqliteDriver(
                    PersistDb.Schema,
                    context,
                    "$dbFilename.db"
            ).apply { PersistDb.Schema.create(this) }
    )
}