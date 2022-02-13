package tech.skot.core.view

typealias SKSpannedString = List<SKSpan>



class SKSpannedStringBuilder {
    val spans:MutableList<SKSpan> = mutableListOf()


    inner class Context(val typeface: SKSpanFormat.TypeFace? = null,
                        val colored: SKSpanFormat.Colored? = null,
                        val sized: SKSpanFormat.Sized? = null,
                        val underline: Boolean = false,
                        val striked: Boolean = false,
                        val clickable: SKSpanFormat.Clickable? = null) {

        private fun copy(typeface: SKSpanFormat.TypeFace? = null,
                         colored: SKSpanFormat.Colored? = null,
                         sized: SKSpanFormat.Sized? = null,
                         underline: Boolean? = null,
                         striked: Boolean? = null,
                         clickable: SKSpanFormat.Clickable? = null): Context {
            return Context(typeface = typeface ?: this.typeface, colored = colored ?: this.colored, sized = sized ?: this.sized, underline = underline ?: this.underline, striked = striked ?: this.striked, clickable = clickable ?: this.clickable)
        }

        fun append(text:String) {
            spans.add(SKSpan(text, typeface, colored, sized, underline, striked, clickable))
        }

        fun bold(block: Context.()->Unit) {
            copy(typeface = SKSpanFormat.Bold).apply(block)
        }

        fun font(font:Font, block: Context.()->Unit) {
            copy(typeface = SKSpanFormat.WithFont(font)).apply(block)
        }

        fun colored(color:Color, block: Context.()->Unit) {
            copy(colored = SKSpanFormat.Colored(color)).apply(block)
        }

        fun scale(scale:Float, block: Context.()->Unit) {
            copy(sized = SKSpanFormat.Sized(scale)).apply(block)
        }

        fun clickable(onTap:()->Unit, block: Context.()->Unit) {
            copy(clickable = SKSpanFormat.Clickable(onTap)).apply(block)
        }

        fun underline(block: Context.()->Unit) {
            copy(underline = true).apply(block)
        }

        fun striked(block: Context.()->Unit) {
            copy(striked = true).apply(block)
        }

    }


    fun toSKSpannedString() = spans
}

/**
 * build a formatted String
 * @return an SKSpannedString that you can set to a TextView with extension
 *```
 *             skSpannedString {
 *                 colored(Color(R.color.red)) {
 *
 *                     clickable(toast("lien")) {
 *                         append("coucoulien")
 *
 *                         bold {
 *                             append(" rouge")
 *                         }
 *                         scale(1.4f) {
 *                             colored(Color(R.color.green)) {
 *                                 append(" vert ")
 *                             }
 *                             append("re-rouge")
 *                         }
 *                         append("lien encore")
 *                     }
 *
 *                     append("plus lien")
 *                }
 *            }
 *```
 *
 * and in your view:
 *
 * TextView.setSpannedString(SKSpannedString)
 *
 */
fun skSpannedString(block:SKSpannedStringBuilder.Context.()->Unit):SKSpannedString {
    return SKSpannedStringBuilder().apply {
        Context().apply(block)
    }.toSKSpannedString()
}