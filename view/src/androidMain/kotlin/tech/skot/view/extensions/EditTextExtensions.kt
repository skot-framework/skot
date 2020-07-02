package tech.skot.view.extensions

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setOnDone(onDone:((str:String)->Unit)?) {
    if (onDone != null) {
        imeOptions = EditorInfo.IME_ACTION_DONE
        setOnEditorActionListener { tv, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event?.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                tv?.text?.toString()?.let { onDone(it) }
                true
            } else {
                false
            }

        }
    }
    else {
        setOnEditorActionListener { _,_,_ ->
            false
        }
    }
}