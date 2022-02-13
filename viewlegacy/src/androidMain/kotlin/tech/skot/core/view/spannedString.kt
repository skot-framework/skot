package tech.skot.core.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun List<SKSpan>.toCharSequence(context: Context): CharSequence {
    return SpannableStringBuilder().apply {
        this@toCharSequence.forEach { span ->
            val spanIndex = length
            append(span.text)

            when (val typeFace = span.typeface) {
                SKSpanFormat.Bold -> {
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        spanIndex,
                        length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                is SKSpanFormat.WithFont -> {
                    ResourcesCompat.getFont(context, typeFace.font.res)?.let { fontsTypeFace ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            setSpan(
                                TypefaceSpan(fontsTypeFace),
                                spanIndex,
                                length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                }
            }
            span.colored?.color?.let { color ->
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, color.res)),
                    spanIndex,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            span.sized?.scale?.let { scale ->
                setSpan(
                    RelativeSizeSpan(scale),
                    spanIndex,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            span.clickable?.onTap?.let { onTap ->
                setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        onTap()
                    }

                    override fun updateDrawState(ds: TextPaint) {

                    }

                }, spanIndex, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (span.underline) {
                setSpan(UnderlineSpan(), spanIndex, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            if (span.striked) {
                setSpan(StrikethroughSpan(), spanIndex, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }


        }
    }
}


fun TextView.setSpannedString(skSpannedString: List<SKSpan>) {
    text = skSpannedString.toCharSequence(context)
    if (skSpannedString.any { it.clickable != null }) {
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }
}