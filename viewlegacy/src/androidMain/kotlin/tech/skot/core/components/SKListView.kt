package tech.skot.core.components

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*

class SKListView(
    vertical: Boolean,
    reverse: Boolean,
    nbColumns: Int?,
    animate: Boolean,
    animateItem: Boolean,
    proxy:SKListViewProxy,
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

        override fun getItemViewType(position: Int) = items[position].first.layoutId
            ?: throw IllegalStateException("${items[position].first::class.simpleName} can't be in a recyclerview")

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            items[position].let { proxy ->
                val componentViewImpl =
                    proxy.first.bindToItemView(activity, fragment, holder.itemView)
                holder.componentView = componentViewImpl
                mapProxyIndexComponentViewImpl[proxy.first] = componentViewImpl
            }
        }

        override fun onViewRecycled(holder: ViewHolder) {
            super.onViewRecycled(holder)
            holder.componentView?.removeObservers()
            holder.componentView = null

        }


    }

    private val mapProxyIndexComponentViewImpl =
        mutableMapOf<SKComponentViewProxy<*>, SKComponentView<*>>()

    private val adapter = Adapter()

    var items: List<Pair<SKComponentViewProxy<*>, Any>> = emptyList()
        set(newVal) {
            field.forEach { proxy ->
                if (!newVal.any { it.first == proxy.first }) {
                    mapProxyIndexComponentViewImpl[proxy.first]?.removeObservers()
                    mapProxyIndexComponentViewImpl.remove(proxy.first)
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


    fun onItems(items: List<Pair<SKComponentViewProxy<*>, Any>>) {
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
        private val oldList: List<Pair<SKComponentViewProxy<*>, Any>>,
        private val newList: List<Pair<SKComponentViewProxy<*>, Any>>
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
            return oldItem.first.layoutId == newItem.first.layoutId && oldItem.second == newItem.second
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].first == newList[newItemPosition].first
        }

    }
}