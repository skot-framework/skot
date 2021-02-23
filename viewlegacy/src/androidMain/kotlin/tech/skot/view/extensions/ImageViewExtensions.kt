package tech.skot.view.extensions

import android.widget.ImageView
import tech.skot.core.view.Icon

fun ImageView.setIcon(icon:Icon) {
    setImageResource(icon.res)
}