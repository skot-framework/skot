package tech.skot.core

data class SKUri(
    val scheme: String?,
    val host: String?,
    val pathSegments: List<String>,
    val parameters: Map<String, List<String>>,
    val url:String
) {
    fun urlWithoutParameters():String {
        return "$scheme://$host/${pathSegments.joinToString("/")}"
    }

    val shortHost: String? by lazy {
        host?.let {
            val tab = it.split(".")
            if (tab.size>2) {
                tab.takeLast(2).joinToString(".")
            }
            else {
                it
            }
        }
    }
}

expect fun String.toSKUri(): SKUri?