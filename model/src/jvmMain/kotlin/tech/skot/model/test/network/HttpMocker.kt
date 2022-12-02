package tech.skot.model.test.network

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import tech.skot.core.SKLog
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
    var pathPrefix: String? = null
    var nextResponse: HttpResponse? = null
    val responsesForUrl: MutableMap<String, HttpResponse> = mutableMapOf()

    fun setResponseOk(encodedPath: String, content: String = "") {
        responsesForUrl[encodedPath] = HttpResponse(content = content)
    }

    fun setResponse400(encodedPath: String, content: String = "") {
        responsesForUrl[encodedPath] =
            HttpResponse(status = HttpStatusCode.BadRequest, content = content)
    }

    fun HttpMocker.setResponse401(encodedPath: String, content: String = "") {
        responsesForUrl[encodedPath] =
            HttpResponse(status = HttpStatusCode.Unauthorized, content = content)
    }

    fun setResponse404(encodedPath: String) {
        responsesForUrl[encodedPath] = _404
    }

    fun setResponse409(encodedPath: String, content: String = "") {
        responsesForUrl[encodedPath] =
            HttpResponse(status = HttpStatusCode.Conflict, content = content)
    }

    fun setResponse500(encodedPath: String, content: String = "") {
        responsesForUrl[encodedPath] =
            HttpResponse(status = HttpStatusCode.InternalServerError, content = content)
    }

    fun HttpMocker.setResponseWithException(encodedPath: String, throwable: Throwable) {
        responsesForUrl[encodedPath] = HttpResponse(then = {
            throw throwable
        })
    }

    val calls: MutableList<HttpRequestData> = mutableListOf()

    fun init() {
        nextResponse = null
        responsesForUrl.clear()
        calls.clear()
    }

    suspend fun assertJustCalled(encodedPath: String, body: String? = null, rule: String = "") {
        assertTrue("$rule-> le dernier appel http doit être à $encodedPath") {
            calls.lastOrNull()?.pathEqualTo(encodedPath) == true
        }

        body?.let {
            assertEquals(
                message = "$rule-> l'appel à $encodedPath doit avoir le body demandé",
                expected = it,
                actual = calls.lastOrNull()?.body?.toByteReadPacket()?.readText()
            )
        }

    }

    fun assertNotCalled(encodedPath: String) {
        assertTrue(calls.none {
            it.url.encodedPath == encodedPath
        })
    }

    fun assertCallWithParameter(encodedPath: String) {
        val call = calls.find { it.url.encodedPath == encodedPath }
    }

    private fun HttpRequestData.pathEqualTo(path: String): Boolean {
        return url.encodedPath == path || pathPrefix?.let { url.encodedPath.substringAfter(it) == path } == true
    }

    fun findCall(encodedPath: String): HttpRequestData? =
        calls.find { it.pathEqualTo(encodedPath) }

    val nbCalls: Int
        get() = calls.size

    suspend fun assertCalled(encodedPath: String, body: String? = null, rule: String = "") {
        assertTrue("$rule-> un appel à $encodedPath doit avoir été fait") {
            calls.any {
                it.pathEqualTo(encodedPath)
            }
        }

        body?.let {
            assertEquals(
                message = "$rule-> l'appel à $encodedPath doit avoir le body demandé",
                expected = it,
                actual = calls.find { it.pathEqualTo(encodedPath) }?.body?.toByteReadPacket()
                    ?.readText()
            )
        }
    }


    fun request(request: HttpRequestData, scope: MockRequestHandleScope): HttpResponseData {
        SKLog.network("HttpMocker request=$request")
        calls.add(request)
        val requestPath = request.url.encodedPath
        val response = mockHttp.nextResponse ?: mockHttp.responsesForUrl[requestPath]
        ?: pathPrefix?.let {
            mockHttp.responsesForUrl[requestPath.substringAfter(it)]
        }
        ?: throw Exception("Pas de réponse mockée définie pour ${request.url.encodedPath}")
        return response.let {
            it.then?.invoke(mockHttp)
            it.toRespondData(scope)
        }
    }
}

val mockHttp: HttpMocker = HttpMocker()

val mockHttpEngine: MockEngine = MockEngine { request ->
    mockHttp.request(request, this)
}