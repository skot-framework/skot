package tech.skot.core.view

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import tech.skot.core.SKLog
import tech.skot.core.toColor

fun List<SKSpan>.toCharSequence(context: Context): CharSequence {
    return SpannableStringBuilder().apply {
        this@toCharSequence.forEach { span ->
            span.startIcon?.let { icon ->
                append(" ")
                setSpan(
                    if (icon.scale == 1f) {
                        ImageSpan(context, icon.icon.res)
                    } else {
                        ImageSpan(context.resources.getDrawable(icon.icon.res, null).apply {
                            setBounds(0,
                                0,
                                (intrinsicWidth * icon.scale).toInt(),
                                (intrinsicHeight * icon.scale).toInt())
                        })
                    },
                    length - 1,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            val spanIndex = length
            append(span.text)
            val format = span.format
            when (val typeFace = format.typeface) {
                SKSpan.Bold -> {
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        spanIndex,
                        length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                is SKSpan.WithFont -> {
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
            format.color?.let { color ->
                setSpan(
                    ForegroundColorSpan(color.toColor(context)),
                    spanIndex,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            format.scale?.let { scale ->
                setSpan(
                    RelativeSizeSpan(scale),
                    spanIndex,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            format.onTap?.let { onTap ->
                setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        onTap()
                    }

                    override fun updateDrawState(ds: TextPaint) {

                    }

                }, spanIndex, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (format.underline) {
                setSpan(UnderlineSpan(), spanIndex, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            if (format.striked) {
                setSpan(StrikethroughSpan(), spanIndex, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }


        }
    }
}


fun TextView.setSpannedString(skSpannedString: List<SKSpan>) {
    text = skSpannedString.toCharSequence(context)
    if (skSpannedString.any { it.format.onTap != null }) {
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }
}