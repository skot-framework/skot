package tech.skot.core

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import tech.skot.core.view.*
import android.graphics.Color as AndroidColor


fun Color.toColor(context: Context): Int {
    return when (this) {
        is ColorHex -> AndroidColor.parseColor(this.color)
        is ColorRef -> ContextCompat.getColor(context, this.res)
        else -> android.graphics.Color.WHITE
    }
}

fun View.setBackground(resource: Resource) {
    when (resource) {
        is Icon -> this.setBackgroundResource(resource.res)
        is Color -> this.setBackgroundColor(resource.toColor(this.context))
    }
}

fun View.setBackgroundTint(color: Color) {
    this.backgroundTintList = ColorStateList.valueOf(color.toColor(this.context))
}

fun ImageView.setImageTint(color: Color) {
    this.imageTintList = ColorStateList.valueOf(color.toColor(this.context))
}

fun Drawable.setTint(context: Context, color: Color): Drawable {
    val wrappedDrawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrappedDrawable, color.toColor(context))
    return wrappedDrawable
}

fun Dimen.toPixelSize(context: Context): Int {
    return when (this) {
        is DimenDP -> (this.dp * Resources.getSystem().displayMetrics.density).toInt()
        is DimenRef -> context.resources.getDimensionPixelSize(this.res)
        else -> {
            throw Exception("Dimen.toPixelSize can only be used with ${DimenDP::class.qualifiedName} and ${DimenRef::class.qualifiedName}, create a custom extension for custom dimen ${this::class.simpleName}")
        }
    }
}