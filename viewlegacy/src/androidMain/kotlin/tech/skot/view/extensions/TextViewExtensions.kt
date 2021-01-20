package tech.skot.view.extensions

import android.graphics.Paint
import android.widget.TextView

fun TextView.underline(state: Boolean = true) {
    paintFlags = if (state) {
        paintFlags or Paint.UNDERLINE_TEXT_FLAG
    } else 0
}

fun TextView.strike(state: Boolean = true) {
    paintFlags = if (state) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else 0
}

fun TextView.setTextOrGone(newText:CharSequence?) {
    text = newText
    setVisible(!text.isNullOrBlank())
}
