package tech.skot.view.extensions

import android.view.View

fun View.setVisible(state:Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}