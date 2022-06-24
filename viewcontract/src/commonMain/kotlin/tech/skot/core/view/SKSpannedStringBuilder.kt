package tech.skot.core.view

typealias SKSpannedString = List<SKSpan>


class SKSpannedStringBuilder {
    val spans: MutableList<SKSpan> = mutableListOf()


    inner class Context(val format: SKSpan.Format) {

        fun append(text: String) {
            spans.add(SKSpan(text = text, format = format))
        }

        fun appendIcon(icon: Icon, scale: Float = 1f) {
            spans.add(
                SKSpan(
                    text = "",
                    startIcon = SKSpan.Icon(icon = icon, scale = scale),
                    format = format))
        }

        fun bold(block: Context.() -> Unit) {
            Context(format = format.copy(typeface = SKSpan.Bold)).apply(block)
        }

        fun font(font: Font, block: Context.() -> Unit) {
            Context(format = format.copy(typeface = SKSpan.WithFont(font))).apply(block)
        }

        fun colored(color: Color, block: Context.() -> Unit) {
            Context(format = format.copy(color = color)).apply(block)
        }

        fun scale(scale: Float, block: Context.() -> Unit) {
            Context(format = format.copy(scale = scale)).apply(block)
        }

        fun clickable(onTap: () -> Unit, block: Context.() -> Unit) {
            Context(format = format.copy(onTap = onTap)).apply(block)
        }

        fun underline(block: Context.() -> Unit) {
            Context(format = format.copy(underline = true)).apply(block)
        }

        fun striked(block: Context.() -> Unit) {
            Context(format = format.copy(striked = true)).apply(block)
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
fun skSpannedString(block: SKSpannedStringBuilder.Context.() -> Unit): SKSpannedString {
    return SKSpannedStringBuilder().apply {
        Context(format = SKSpan.Format()).apply(block)
    }.toSKSpannedString()
}


fun String.skFormatDelemitedString(
    vararg parameter: SKSpan.Format,
    delimiter: String = "##",
): SKSpannedString =
    split(delimiter).filterNot { it.isNullOrBlank() }.mapIndexed { index: Int, text: String ->
        parameter.getOrNull(index)?.span(text) ?: SKSpan(text = text, format = SKSpan.Format())
    }