package tech.skot.android.tests

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.request.header
import io.ktor.request.path
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class ProxyDev(mock: ServicesMock) {

    companion object {
        const val Header_SK_HOST = "SK_Host"
    }

    val server = embeddedServer(Netty, port = 8080) {
        intercept(ApplicationCallPipeline.Call) {
            val host = call.request.header(Header_SK_HOST) ?: ""
            val path = call.request.path()
            val params = call.request.queryParameters.entries().map { it.key to it.value }.toMap()
            mock.respond(this, host, path, params)
        }
    }
}