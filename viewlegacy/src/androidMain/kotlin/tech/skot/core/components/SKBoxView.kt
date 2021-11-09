package tech.skot.core.components

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.view.extensions.setVisible

class SKBoxView(
    proxy: SKBoxViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    viewGroup: ViewGroup
) : SKComponentView<ViewGroup>(proxy, activity, fragment, viewGroup) {


    fun onItems(items: List<SKComponentViewProxy<*>>) {
        binding.removeAllViews()
        subViews.clear()
        items.forEach {
            subViews.add(it.inflateInParentAndBind(activity = activity, fragment = fragment, parent = binding))
        }
    }

    fun onHidden(hidden: Boolean?) {
        hidden?.let { binding.setVisible(!it) }

    }


}