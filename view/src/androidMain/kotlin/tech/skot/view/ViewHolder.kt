package tech.skot.view

import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import tech.skot.view.extensions.setOnClick
import tech.skot.view.extensions.setVisible

open class ViewHolder<V : View>() {

    var visible: Boolean = true
        set(value) {
            field = value
            view?.setVisible(value)
        }

    var view: V? = null
        set(value) {
            field = value
            value?.let { update(it) }
        }

    @CallSuper
    open fun update(v: V) {
        v.apply {
            setVisible(visible)
            _onClick?.let {
                setOnClick(_onClick, _single, _delay)
            }
        }
    }

    private var _onClick: (() -> Unit)? = null
    private var _single: Boolean = true
    private var _delay: Long = 500
    fun setOnClick(myOnClick: (() -> Unit)?, single: Boolean = true, delay: Long = 500) {
        _onClick = myOnClick
        _single = single
        _delay = delay
        view?.setOnClick(myOnClick, single, delay)
    }
}


class TextViewHolder : ViewHolder<TextView>() {

    var text: CharSequence? = null
        set(value) {
            field = value
            view?.text = value
        }

    override fun update(v: TextView) {
        super.update(v)
        v.text = text
    }
}
