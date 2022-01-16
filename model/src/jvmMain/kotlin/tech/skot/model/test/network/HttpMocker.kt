package tech.skot.model.test.network

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

class HttpResponse(
    val content: String = "",
    val status: HttpStatusCode = HttpStatusCode.Accepted,
    val headers: Headers = headersOf(HttpHeaders.ContentType, "application/json"),
    val then: (HttpMocker.() -> Unit)? = null
) {
    fun toRespondData(scope: MockRequestHandleScope): HttpResponseData {
        return scope.respond(
            content = content,
            status = status,
            headers = headers
        )
    }
}

class HttpMocker() {
    var nextResponse: HttpResponse? = null
    val responsesForUrl: MutableMap<String, HttpResponse> = mutableMapOf()

}

val mockHttp: HttpMocker = HttpMocker()

val mockHttpEngine: MockEngine = MockEngine { request ->
    val response = mockHttp.nextResponse ?: mockHttp.responsesForUrl[request.url.encodedPath]
    ?: throw Exception("Pas de réponse mockée définie")
    response?.let {
        it.then?.invoke(mockHttp)
        it.toRespondData(this)
    }
}