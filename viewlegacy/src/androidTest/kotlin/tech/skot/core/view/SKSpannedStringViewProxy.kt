package tech.skot.core.view

import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.viewlegacy.test.R

class SKSpannedStringViewProxy(
    val skSpannedString:List<SKSpan>
): SKComponentViewProxy<TextView>() {


    override val layoutId = R.layout.sk_spanned_string

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: TextView
    ) = SKSpannedStringView(this, activity, fragment, binding).apply {
        setSkSpannedString(skSpannedString)
    }
}

class SKSpannedStringView(
    proxy: SKSpannedStringViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: TextView
): SKComponentView<TextView>(proxy, activity, fragment, binding) {

    fun setSkSpannedString(skSpannedString:List<SKSpan>) {
        binding.setSpannedString(skSpannedString)
    }

}