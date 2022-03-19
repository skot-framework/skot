package tech.skot.core.components.inputs

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.toColor
import tech.skot.view.extensions.setVisible
import tech.skot.view.extensions.strike
import tech.skot.viewlegacy.R
import tech.skot.viewlegacy.databinding.SkComboBinding


class SKComboView(
    override val proxy: SKComboViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: SkComboBinding
) : SKCommonComboView<SkComboBinding>(
    proxy,
    activity,
    fragment,
    binding,
    binding.root,
    binding.autoComplete,
    R.layout.sk_combo_choice_item
) {
    init {
        autoComplete.apply {
            isEnabled = false
            inputType = InputType.TYPE_NULL
        }
    }
}

abstract class SKCommonComboView<Binding : Any>(
    override val proxy: SKCommonComboViewProxy<Binding>,
    activity: SKActivity,
    fragment: Fragment?,
    binding: Binding,
    protected val inputLayout: TextInputLayout,
    protected val autoComplete: AutoCompleteTextView,
    private val choiceItemLayoutID: Int
) : SKComponentView<Binding>(proxy, activity, fragment, binding) {

    private var _adapter: BaseAdapter? = null
    protected var _choices: List<SKComboVC.Choice> = emptyList()

    init {


        autoComplete.apply {

            object : BaseAdapter(), Filterable {
                override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
                    val tv = p1 ?: LayoutInflater.from(context)
                        .inflate(choiceItemLayoutID, viewGroup, false)
                    val choice = _choices[position]

                    (tv as? TextView)?.let { textView ->
                        textView.text = choice.text
                        textView.strike(choice.strikethrough)
                        choice.textColor?.toColor(context)?.let {
                            textView.setTextColor(it)
                        }

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
        if (proxy.oldSchoolModeHint) {
            autoComplete.hint = hint
        } else {
            inputLayout.hint = hint
        }


    }

    fun onError(error: String?) {
        inputLayout.error = error
    }

    protected var lockSelectedReaction = false

    fun onOnSelected(onSelected: ((data: Any?) -> Unit)?) {
        if (onSelected != null) {
//            autoComplete.setOnDismissListener {
//                SKLog.d("---- dans OnDismissListener $lockSelectedReaction")
//            }

            autoComplete.setOnItemClickListener { parent, view, position, id ->
                _choices.getOrNull(position)?.let {
                    onSelected(it.data)
                }

            }
        } else {
            autoComplete.setOnClickListener(null)
        }

    }

    fun onChoices(choices: List<SKComboVC.Choice>) {
        _choices = choices
        _adapter?.notifyDataSetChanged()
    }


    open fun onSelect(selected: SKComboVC.Choice?) {
        lockSelectedReaction = true
        autoComplete.setText(selected?.inputText, false)
        autoComplete.strike(selected?.strikethrough == true)
        selected?.textColor?.toColor(context)?.let {
            autoComplete.setTextColor(it)
        }
        lockSelectedReaction = false
    }

    fun onEnabled(enabled: Boolean?) {
        inputLayout.isEnabled = enabled != false
    }

    fun onHidden(hidden: Boolean?) {
        hidden?.let { inputLayout.setVisible(!it) }
    }


    fun onDropDownDisplayed(state: Boolean) {
        autoComplete.post {
            if (state) {
                autoComplete.showDropDown()
            } else {
                autoComplete.dismissDropDown()
            }
        }

    }

}