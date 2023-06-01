package tech.skot.core.components.presented

import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.viewlegacy.databinding.SkAlertInputBinding

class SKAlertView(override val proxy: SKAlertViewProxy, activity: SKActivity, fragment: Fragment?) : SKComponentView<Unit>(proxy, activity, fragment, Unit) {

    data class State(val state: SKAlertVC.Shown, val alert: AlertDialog)

    private var current: State? = null

    private var editText: EditText? = null

    private var firstInputChangeDoneFor = false

    fun onState(state: SKAlertVC.Shown?) {

        if (state != current?.state) {
            if (state != null) {
                AlertDialog.Builder(context)
                    .setTitle(state.title)
                    .setMessage(state.message)
                    .setOnDismissListener {
                        if (proxy.state == state) {
                            proxy.state = null
                        }
                    }
                        .setCancelable(state.cancelable)
                        .apply {
                            if (state.withInput) {
                                val inputView = SkAlertInputBinding.inflate(
                                    this@SKAlertView.activity.layoutInflater,
                                    null,
                                    false
                                )
                                editText = inputView.et.apply {
                                    setText(proxy.inputText)

                                    addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                        ) {
                                            //na
                                        }

                                        override fun onTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                        ) {
                                            val newText = p0?.toString()
                                            if (firstInputChangeDoneFor || !newText.isNullOrBlank()) {
                                                proxy.inputText = p0?.toString()
                                            }
                                            firstInputChangeDoneFor = true
                                        }

                                        override fun afterTextChanged(p0: Editable?) {
                                            //na
                                        }

                                    })

                                }
                                setView(inputView.root)
                            }
                            setPositiveButton(
                                state.mainButton.label,
                                state.mainButton.action?.let { action ->
                                    DialogInterface.OnClickListener { _, _ -> action() }
                                })

                            state.secondaryButton?.let { button ->
                                setNegativeButton(button.label, button.action?.let { action ->
                                    DialogInterface.OnClickListener { _, _ -> action() }
                                })
                            }
                            state.neutralButton?.let { button ->
                                setNeutralButton(button.label, button.action?.let { action ->
                                    DialogInterface.OnClickListener { _, _ -> action() }
                                })
                            }
                        }
                        .create()
                        .let {
                            current = State(state, it)
                            it.show()
                        }
            } else {
                current?.alert?.dismiss()
                current = null
            }

        }

    }


    fun onInputText(inputText: String?) {
        if (editText?.text?.toString() != inputText) {
            editText?.setText(inputText)
        }
    }
}