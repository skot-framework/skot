package tech.skot.android.tests

import android.os.Environment
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.AttributeKey
import java.io.File

class Record {

    class Config

    val baseDirectory by lazy {
        Environment.getExternalStoragePublicDirectory(
                "Records")
    }

    fun getRecordDirectory(directoryName: String): File {
        if (!baseDirectory.exists()) {
            baseDirectory.mkdir()
        }
        val directory = File(baseDirectory, directoryName)
        if (!directory.exists()) {
            directory.mkdir()
        }
        return directory
    }

    companion object Feature : HttpClientFeature<Unit, Record> {
        override val key: AttributeKey<Record> = AttributeKey("Record")
        override fun prepare(block: Unit.() -> Unit): Record {
            return Record()
        }

        override fun install(feature: Record, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.After) {
                (subject as? HttpResponseContainer)?.let { httpCont ->
                    (httpCont.response as? String)?.let {
                        val path = context.request.url.encodedPath
                        val dir = feature.getRecordDirectory(path.replace('/', '_'))
                        val file = File(dir, "${System.currentTimeMillis().toString()}_${context.response.status.value.toString()}.json")
                        file.writeText(it)

                    }
                }
            }


        }
    }

}