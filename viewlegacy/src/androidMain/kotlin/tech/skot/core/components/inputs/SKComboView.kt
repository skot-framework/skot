package tech.skot.core.components.inputs

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.view.extensions.strike
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkComboBinding

class SKComboView(
    activity: SKActivity,
    fragment: Fragment?,
    binding: SkComboBinding
): SKComponentView<SkComboBinding>(activity, fragment, binding) {

    private var _adapter: BaseAdapter? = null
    private var _choices:List<SKComboVC.Choice> = emptyList()

    init {
        binding.dropdown.apply {
            inputType = InputType.TYPE_NULL
            isEnabled = false

            object : BaseAdapter(), Filterable {
                override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
                    val tv = LayoutInflater.from(context).inflate(R.layout.sk_combo_choice_item, viewGroup, false)
                    val choice = _choices[position]
                    (tv as? TextView)?.let { textView ->
                        textView.text = choice.text
                        textView.strike(choice.strikethrough)
                        textView.setTextColor(ContextCompat.getColor(context, if (choice.colored) R.color.sk_combo_choice_text_colored_color else R.color.sk_combo_choice_text_color))
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
        binding.root.hint = hint
    }

    var lockSelectedReaction = false

    fun onOnSelected(onSelected: (newText: String) -> Unit) {
        binding.dropdown.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!lockSelectedReaction) {
                    p0?.let { onSelected(it.toString()) }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // na
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // na
            }
        })
    }

    fun onChoices(choices:List<SKComboVC.Choice>) {
        _choices = choices
        _adapter?.notifyDataSetChanged()
    }

    fun onSelect(selected: SKComboVC.Choice?) {
        SKLog.d("--------- onSelect $selected")
        lockSelectedReaction = true
        binding.dropdown.setText(selected?.text, false)
        binding.dropdown.strike(selected?.strikethrough == true)
        binding.dropdown.setTextColor(ContextCompat.getColor(activity, if (selected?.colored == true) R.color.sk_combo_choice_text_colored_color else R.color.sk_combo_choice_text_color))
        lockSelectedReaction = false
    }

    fun onEnabled(enabled:Boolean?) {
        binding.root.isEnabled = enabled != false
    }

}