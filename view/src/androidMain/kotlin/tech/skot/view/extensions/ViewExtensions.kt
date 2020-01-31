package tech.skot.view.extensions

import android.view.View

fun View.setVisible(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun View.setOnClick(myOnClick: (() -> Unit)?, single: Boolean = true, delay: Long = 500) {
    if (!single) {
        setOnClickListener { myOnClick?.invoke() }
    } else {
        setOnClickListener(object : View.OnClickListener {
            var lastClick: Long = 0
            override fun onClick(v: View?) {
                val now = System.currentTimeMillis()
                if (now - lastClick > delay) {
                    lastClick = now
                    myOnClick?.invoke()
                }
            }
        })
    }

}