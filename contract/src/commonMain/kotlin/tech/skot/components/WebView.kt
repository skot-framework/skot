package tech.skot.components

interface WebView :ComponentView {

    val userAgent:String?
    val redirect:List<RedirectParam>

    fun openUrl(url:String, onFinished:(()->Unit)? = null, javascriptOnFinished:String? = null, onError:(()->Unit)? = null, post:Map<String,String>? = null)

    abstract class RedirectParam {
        abstract fun matches(url:String):Boolean
        abstract val onRedirect:(path:String, params:Map<String,String>)->Boolean

        class Start(private val start:String, override val onRedirect:(path:String, params:Map<String,String>)->Boolean):RedirectParam() {
            override fun matches(url:String) = url.startsWith(start)
        }

        class Match(private val regex:Regex, override val onRedirect:(path:String, params:Map<String,String>)->Boolean):RedirectParam() {
            override fun matches(url:String) = regex.matches(url)
        }
    }

}