package tech.skot.core

data class SKUri(
    val scheme: String?,
    val host: String?,
    val pathSegments: List<String>,
    val parameters: Map<String, List<String>>
) {
    fun urlWithoutParameters():String {
        return "$scheme://$host/${pathSegments.joinToString("/")}"
    }
}