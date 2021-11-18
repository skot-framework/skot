package tech.skot.core.components

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import tech.skot.core.SKLog

class SKListView(
    vertical: Boolean,
    reverse: Boolean,
    nbColumns: Int?,
    animate: Boolean,
    animateItem: Boolean,
    proxy: SKListViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    private val recyclerView: RecyclerView
) : SKComponentView<RecyclerView>(proxy, activity, fragment, recyclerView) {


    inner class ViewHolder(idLayout: Int, parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(idLayout, parent, false)
    ) {
        var componentView: SKComponentView<*>? = null
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(viewType, parent)

        override fun getItemViewType(position: Int) = items[position].component.layoutId
            ?: throw IllegalStateException("${items[position].component::class.simpleName} can't be in a recyclerview")

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            items[position].let { proxy ->
                val componentViewImpl =
                    proxy.component.bindToItemView(activity, fragment, holder.itemView)
                holder.componentView = componentViewImpl
                mapProxyIndexComponentViewImpl[proxy.component] = componentViewImpl
            }
        }

        override fun onViewRecycled(holder: ViewHolder) {
            super.onViewRecycled(holder)
            holder.componentView?.onRecycle()
            holder.componentView = null

        }


    }

    private val mapProxyIndexComponentViewImpl =
        mutableMapOf<SKComponentViewProxy<*>, SKComponentView<*>>()

    private val adapter = Adapter()

    override fun onRecycle() {
        super.onRecycle()
        mapProxyIndexComponentViewImpl.values.forEach {
            it.onRecycle()
        }
    }

    var items: List<SKListViewProxy.Item> = emptyList()
        set(newVal) {
            field.forEach { proxy ->
                if (!newVal.any { it.component == proxy.component }) {
                    mapProxyIndexComponentViewImpl.remove(proxy.component)
                }
            }
            val diffCallBack = DiffCallBack(field, newVal)
            field = newVal
            DiffUtil.calculateDiff(diffCallBack, true).dispatchUpdatesTo(adapter)
        }


    init {
        recyclerView.layoutManager =
            if (nbColumns != null) {
                GridLayoutManager(
                    context,
                    nbColumns,
                    if (vertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL,
                    reverse
                )
            } else {
                LinearLayoutManager(
                    context,
                    if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
                    reverse
                )
            }

        when {
            !animate -> {
                recyclerView.itemAnimator = null
            }
            !animateItem -> {
                (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            }
        }

        recyclerView.adapter = adapter
    }


    fun onItems(items: List<SKListViewProxy.Item>) {
        this.items = items
    }

    fun saveState(): Parcelable? {
        return recyclerView.layoutManager?.onSaveInstanceState()
    }

    fun restoreState(state: Parcelable) {
        recyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    fun scrollToPosition(position: Int) {
        (binding.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0)
    }


    class DiffCallBack(
        private val oldList: List<SKListViewProxy.Item>,
        private val newList: List<SKListViewProxy.Item>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.component.layoutId == newItem.component.layoutId && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].component == newList[newItemPosition].component
        }

    }
}

abstract class SKListItemTouchHelperCallBack(private val listView: SKListView) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listView.items[viewHolder.absoluteAdapterPosition].onSwipe?.invoke()
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val position = viewHolder.adapterPosition
        val item = if (position >= 0) listView.items.getOrNull(position) else null
        if (item == null) {
            SKLog.e(IllegalStateException("getSwipeDirs item null"), "getSwipeDirs item null")
        }
        return if (item?.onSwipe != null) {
            super.getSwipeDirs(recyclerView, viewHolder)
        } else {
            0
        }
    }

}