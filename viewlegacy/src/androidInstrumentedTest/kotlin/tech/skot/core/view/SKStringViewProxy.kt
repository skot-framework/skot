package tech.skot.core.view

import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.viewlegacy.test.R

class SKStringViewProxy(
    val content:CharSequence
): SKComponentViewProxy<TextView>() {


    override val layoutId = R.layout.sk_spanned_string

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TextView
    ) = SKStringView(this, activity, fragment, binding).apply {
        setString(content)
    }
}

class SKStringView(
    proxy: SKStringViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: TextView
): SKComponentView<TextView>(proxy, activity, fragment, binding) {

    fun setString(content: CharSequence) {
        binding.text = content
    }

}