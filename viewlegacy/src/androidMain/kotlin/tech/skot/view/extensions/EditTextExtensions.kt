package tech.skot.view.extensions

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.setOnDone(onDone:((str:String?)->Unit)?) {
    if (onDone != null) {
        imeOptions = EditorInfo.IME_ACTION_DONE
        setOnEditorActionListener { tv, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event?.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onDone(tv?.text?.toString())
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

fun EditText.setOnNotNullDone(onDone:((str:String)->Unit)?) {
    setOnDone(onDone?.let {action ->
        {
            it?.let { action(it) }
        }
    })
}