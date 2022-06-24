package tech.skot.core.view


data class SKSpan(
    val text:String,
    val format:Format,
    val startIcon: Icon? = null
) {

    data class Format(
        val typeface: TypeFace? = null,
        val color: Color? = null,
        val scale: Float? = null,
        val underline:Boolean = false,
        val striked:Boolean = false,
        val onTap: (()->Unit)? = null,
    ) {
        fun span(text:String):SKSpan = SKSpan(text = text, format = this)
    }

    abstract class TypeFace
    object Bold: TypeFace()
    data class WithFont(val font:Font): TypeFace()

}



