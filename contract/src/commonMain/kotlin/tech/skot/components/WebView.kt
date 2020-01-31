package tech.skot.components

interface WebView :ComponentView {
    val redirect:List<RedirectParam>

    fun openUrl(url:String, onFinished:(()->Unit)? = null, onError:(()->Unit)? = null)

    class RedirectParam(val start:String, val onRedirect:(path:String, params:Map<String,String>)->Unit) {
        fun matches(url:String) = url.startsWith(start)
    }
}