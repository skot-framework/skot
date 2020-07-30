package tech.skot.components

interface WebView :ComponentView {

    val userAgent:String?
    val redirect:List<RedirectParam>

    fun openUrl(url:String, onFinished:(()->Unit)? = null, javascriptOnFinished:String? = null, onError:(()->Unit)? = null, post:Map<String,String>? = null)

    class RedirectParam(val start:String, val onRedirect:(path:String, params:Map<String,String>)->Boolean) {
        fun matches(url:String) = url.startsWith(start)
    }
}