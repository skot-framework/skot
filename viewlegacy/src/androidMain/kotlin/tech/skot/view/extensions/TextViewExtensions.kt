package tech.skot.view.extensions

import android.graphics.Paint
import android.widget.TextView

fun TextView.underline(state: Boolean = true) {
    paintFlags = if (state) {
        paintFlags or Paint.UNDERLINE_TEXT_FLAG
    } else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}

fun TextView.strike(state: Boolean = true) {
    paintFlags = if (state) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

fun TextView.setTextOrGone(newText:CharSequence?) {
    text = newText
    setVisible(!text.isNullOrBlank())
}