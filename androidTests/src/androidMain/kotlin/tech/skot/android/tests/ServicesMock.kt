package tech.skot.android.tests

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

abstract class ServicesMock {
    interface Mocker {
        val respond: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
    }

    open class Respond(val status: HttpStatusCode, val content: Any) : Mocker {
        override val respond: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit = {
            call.respond(status, content)
        }

    }

    val httpRequestModifier: HttpRequestBuilder.() -> Unit = {
        if (willMock(url)) {
            headers.append(ProxyDev.Header_SK_HOST, url.host)
            url {
                host = "localhost"
                port = 8080
                protocol = URLProtocol.HTTP
            }
        }
    }

    object Error404 : Respond(HttpStatusCode.NotFound, "Erreur 404 générique")


    abstract class Service {
        abstract fun matches(host: String, path: String, queryParameters: Map<String, List<String>>): Boolean
        var mock: Mocker? = null

        fun willMock(host: String, path: String, queryParameters: Map<String, List<String>>) =
                if (mock != null) matches(host, path, queryParameters) else false

        class EndsWith(private val suffix: String) : Service() {
            override fun matches(host: String, path: String, queryParameters: Map<String, List<String>>): Boolean {
                return path.endsWith(suffix)
            }

            override fun toString(): String {
                return "Service.EndsWith(\"$suffix\")"
            }
        }

        class WithParameter(private val name: String, private val value: String) : Service() {
            override fun matches(host: String, path: String, queryParameters: Map<String, List<String>>): Boolean {
                return queryParameters[name]?.contains(value) == true
            }

            override fun toString(): String {
                return "Service.WithParameter(\"$name\",\"$value\")"
            }
        }
    }

    abstract val services: List<Service>

    fun willMock(url: URLBuilder) = services.any {
        it.willMock(url.host, url.encodedPath, url.parameters.entries().map { it.key to it.value }.toMap())
    }

    suspend fun respond(pipeline: PipelineContext<Unit, ApplicationCall>, host: String, path: String, queryParameters: Map<String, List<String>>) {
        services.forEach {
            if (it.willMock(host, path, queryParameters)) {
                return@forEach it.mock?.let { it.respond.invoke(pipeline) }
                        ?: pipeline.call.respond(HttpStatusCode.BadRequest, "Service non mocké pas normal")
            }
        }
    }

}