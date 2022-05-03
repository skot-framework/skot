package tech.skot.core.components

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tech.skot.core.SKLog
import tech.skot.view.extensions.setVisible

class SKBoxView(
    proxy: SKBoxViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    viewGroup: ViewGroup
) : SKComponentView<ViewGroup>(proxy, activity, fragment, viewGroup) {


    private var currentViews:List<View>? = null

    private var firstItems = true
    fun onItems(items: List<SKComponentViewProxy<*>>) {
        binding.removeAllViews()
        subViews.clear()
        if (!firstItems) {
            binding.post {
                setItems(items)
            }
        }
        else {
            setItems(items)
        }

    }

    private fun setItems(items: List<SKComponentViewProxy<*>>) {
        items.forEach { newItemProxy ->
            val existingView = currentViews?.find { it.tag == newItemProxy.hashCode() }
            if (existingView != null) {
                binding.addView(existingView)
            }
            else {
                newItemProxy.inflateInParentAndBind(activity = activity, fragment = fragment, parent = binding)
            }
        }
        val newCurrentViews = mutableListOf<View>()
        (0 until binding.childCount).forEach {
            newCurrentViews.add(binding.getChildAt(it))
        }
        currentViews = newCurrentViews
    }

    fun onHidden(hidden: Boolean?) {
        hidden?.let { binding.setVisible(!it) }

    }


}