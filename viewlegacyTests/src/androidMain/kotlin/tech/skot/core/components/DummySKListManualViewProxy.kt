package tech.skot.core.components

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class DummySKListManualViewProxy(
    customLayoutId:Int,
    private val layoutManager:RecyclerView.LayoutManager
): SKListViewProxy(
    layoutMode = SKListVC.LayoutMode.Manual
) {
    override val layoutId: Int = customLayoutId

    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: RecyclerView
    ): SKListView {
        return super.bindTo(activity, fragment, binding).apply {
            binding.layoutManager = layoutManager
        }
    }

}