package tech.skot.core.components.inputs

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.view.extensions.setVisible
import tech.skot.view.extensions.strike
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkComboBinding

class SKComboView(
    activity: SKActivity,
    fragment: Fragment?,
    binding: SkComboBinding
) : SKCommonComboView<SkComboBinding>(
    activity,
    fragment,
    binding,
    binding.root,
    binding.autoComplete
) {
    init {
        autoComplete.apply {
            isEnabled = false
            inputType = InputType.TYPE_NULL
        }
    }
}

abstract class SKCommonComboView<Binding : Any>(
    activity: SKActivity,
    fragment: Fragment?,
    binding: Binding,
    protected val inputLayout: TextInputLayout,
    protected val autoComplete: AutoCompleteTextView
) : SKComponentView<Binding>(activity, fragment, binding) {

    private var _adapter: BaseAdapter? = null
    protected var _choices: List<SKComboVC.Choice> = emptyList()


    init {
        autoComplete.apply {
//            inputInputType?.let { inputType = it }
//            isEnabled = inputIsEnabled
////            isEnabled = true
//            inputType = inputInputType

            object : BaseAdapter(), Filterable {
                override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
                    val tv = LayoutInflater.from(context)
                        .inflate(R.layout.sk_combo_choice_item, viewGroup, false)
                    val choice = _choices[position]
                    (tv as? TextView)?.let { textView ->
                        textView.text = choice.text
                        textView.strike(choice.strikethrough)
                        textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                if (choice.colored) R.color.sk_combo_choice_text_colored_color else R.color.sk_combo_choice_text_color
                            )
                        )
                    }
                    return tv
                }

                override fun getItem(position: Int): Any {
                    val choice = _choices[position]
                    return choice.text
                }

                override fun getItemId(position: Int): Long {
                    return _choices[position].text.hashCode().toLong()
                }

                override fun getCount() = _choices.size

                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(p0: CharSequence?): FilterResults {
                            return FilterResults()
                        }

                        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                            // na
                        }
                    }
                }
            }.let {
                _adapter = it
                setAdapter(it)
            }
        }
    }


    fun onHint(hint: String?) {
        inputLayout.hint = hint
    }

    protected var lockSelectedReaction = false

    fun onOnSelected(onSelected: ((data: Any?) -> Unit)?) {
        if (onSelected != null) {
            SKLog.d("----- onOnSelected : $onSelected")
            autoComplete.setOnDismissListener {
                SKLog.d("---- dans OnDismissListener $lockSelectedReaction")
            }

            autoComplete.setOnItemClickListener { parent, view, position, id ->
                SKLog.d("---- dans OnItemClickListener $position $id")
                _choices.getOrNull(position)?.let {
                    SKLog.d("-###### $it")
                    onSelected(it.data)
                    SKLog.d("-###### $it   onSelected done")
                }

            }
        } else {
            autoComplete.setOnClickListener(null)
        }


//        autoComplete.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                if (!lockSelectedReaction) {
////                    p0?.let {
////                        SKLog.d("---- dans onOnSelected")
////                        onSelected(it.toString())
////                    }
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                // na
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                // na
//            }
//        })
    }

    fun onChoices(choices: List<SKComboVC.Choice>) {
        _choices = choices
        _adapter?.notifyDataSetChanged()
    }

    open fun onSelect(selected: SKComboVC.Choice?) {
        lockSelectedReaction = true
        SKLog.d("---- dans onSelect selected=${selected?.text}")
        autoComplete.setText(selected?.inputText, false)
        autoComplete.strike(selected?.strikethrough == true)
        autoComplete.setTextColor(
            ContextCompat.getColor(
                activity,
                if (selected?.colored == true) R.color.sk_combo_choice_text_colored_color else R.color.sk_combo_choice_text_color
            )
        )
        lockSelectedReaction = false
    }

    fun onEnabled(enabled: Boolean?) {
        inputLayout.isEnabled = enabled != false
    }

    fun onHidden(hidden: Boolean?) {
        hidden?.let { inputLayout.setVisible(!it) }
    }

    init {
        autoComplete.setOnDismissListener {

        }
    }

    fun onDropDownDisplayed(state: Boolean) {
        SKLog.d("onDropDownDisplayed   $state")
        if (state) {
            autoComplete.showDropDown()
        } else {
            autoComplete.dismissDropDown()
        }
    }

}