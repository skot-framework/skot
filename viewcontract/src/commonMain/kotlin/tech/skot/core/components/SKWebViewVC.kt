package tech.skot.core.components

@SKLayoutIsSimpleView
interface SKWebViewVC: SKComponentVC {
    val config:Config
    var openUrl:OpenUrl?
    var goBack:BackRequest?

    data class Config(val userAgent:String?, val redirect:List<RedirectParam> = emptyList())
    data class OpenUrl(val url:String, val onFinished:(()->Unit)? = null, val javascriptOnFinished:String? = null, val onError:(()->Unit)? = null, val post:Map<String,String>? = null)

    sealed class RedirectParam {
        abstract fun matches(url:String):Boolean
        abstract val onRedirect:(path:String, params:Map<String,String>)->Boolean

        class Start(private val start:String, override val onRedirect:(path:String, params:Map<String,String>)->Boolean):RedirectParam() {
            override fun matches(url:String) = url.startsWith(start)
        }

        class Match(private val regex:Regex, override val onRedirect:(path:String, params:Map<String,String>)->Boolean):RedirectParam() {
            override fun matches(url:String) = regex.matches(url)
        }
    }

    data class BackRequest(val onCantBack:(()->Unit)? = null)
}