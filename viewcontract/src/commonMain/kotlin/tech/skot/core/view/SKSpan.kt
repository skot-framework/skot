package tech.skot.core.view


data class SKSpan(
    val text:String,
    val typeface: SKSpanFormat.TypeFace? = null,
    val colored: SKSpanFormat.Colored? = null,
    val sized: SKSpanFormat.Sized? = null,
    val clickable: SKSpanFormat.Clickable? = null
)


sealed class SKSpanFormat {
    abstract class TypeFace: SKSpanFormat()
    object Bold: TypeFace()
    data class WithFont(val font:Font): TypeFace()

    data class Colored(val color:Color): SKSpanFormat()
    /**
     * Relative size
     */
    data class Sized(val scale:Float): SKSpanFormat()

    data class Clickable(val onTap:()->Unit): SKSpanFormat()
}

