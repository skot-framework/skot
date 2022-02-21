package tech.skot.model.test.network

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import tech.skot.model.test.network.HttpResponse.Companion._404
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    companion object {
        val _404 = HttpResponse(status = HttpStatusCode.NotFound)
    }
}


class HttpMocker() {
    var nextResponse: HttpResponse? = null
    val responsesForUrl: MutableMap<String, HttpResponse> = mutableMapOf()

    fun setResponseOk(encodedPath: String, content: String = "") {
        responsesForUrl[encodedPath] = HttpResponse(content = content)
    }

    fun setResponse400(encodedPath: String, content:String = "") {
        responsesForUrl[encodedPath] = HttpResponse(status = HttpStatusCode.BadRequest,content = content)
    }

    fun setResponse404(encodedPath: String) {
        responsesForUrl[encodedPath] = _404
    }

    fun setResponse500(encodedPath: String, content:String = "") {
        responsesForUrl[encodedPath] = HttpResponse(status = HttpStatusCode.InternalServerError,content = content)
    }

    val calls: MutableList<HttpRequestData> = mutableListOf()

    fun init() {
        nextResponse = null
        responsesForUrl.clear()
        calls.clear()
    }

    suspend fun assertJustCalled(encodedPath: String, body: String? = null, rule: String = "") {
        assertTrue("$rule-> le dernier appel http doit être à $encodedPath") {
            calls.lastOrNull()?.url?.encodedPath == encodedPath
        }

        body?.let {
            assertEquals(
                message = "$rule-> l'appel à $encodedPath doit avoir le body demandé",
                expected = it,
                actual = calls.lastOrNull()?.body?.toByteReadPacket()?.readText()
            )
        }

    }

    suspend fun assertCalled(encodedPath:String, body:String? = null, rule:String = "") {
        assertTrue("$rule-> un appel à $encodedPath doit avoir été fait") {
            calls.any {
                it.url.encodedPath == encodedPath
            }
        }

        body?.let {
            assertEquals(
                message = "$rule-> l'appel à $encodedPath doit avoir le body demandé",
                expected = it,
                actual = calls.find { it.url.encodedPath == encodedPath }?.body?.toByteReadPacket()?.readText()
            )
        }
    }


}

val mockHttp: HttpMocker = HttpMocker()

val mockHttpEngine: MockEngine = MockEngine { request ->

    mockHttp.calls.add(request)
    val response = mockHttp.nextResponse ?: mockHttp.responsesForUrl[request.url.encodedPath]
    ?: throw Exception("Pas de réponse mockée définie pour ${request.url.encodedPath}")
    response?.let {
        it.then?.invoke(mockHttp)
        it.toRespondData(this)
    }
}