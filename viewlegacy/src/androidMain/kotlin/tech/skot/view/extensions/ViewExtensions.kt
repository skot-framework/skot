package tech.skot.view.extensions

import android.view.View

fun View.setVisible(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun View.setVisibleInvisible(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.INVISIBLE
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


fun View.updatePadding(left:Int? =null, top:Int? = null, right:Int? = null, bottom:Int? = null) {
    setPadding(left ?:paddingLeft, top ?: paddingTop, right ?: paddingRight, bottom ?: paddingBottom)
}