package tech.skot.core.components

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SKListView(vertical: Boolean, reverse: Boolean, nbColumns: Int?, activity: SKActivity, fragment: Fragment?, private val recyclerView: RecyclerView) : SKComponentView<RecyclerView>(activity, fragment, recyclerView) {


    inner class ViewHolder(idLayout: Int, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(idLayout, parent, false)) {
        var componentView: SKComponentView<*>? = null
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(viewType, parent)

        override fun getItemViewType(position: Int) = items[position].layoutId
                ?: throw IllegalStateException("${items[position]::class.simpleName} can't be in a recyclerview")

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            items[position].let { proxy ->
                val componentViewImpl = proxy.bindToItemView(activity, fragment, holder.itemView)
                holder.componentView = componentViewImpl
                mapProxyIndexComponentViewImpl[proxy] = componentViewImpl
            }
        }

        override fun onViewRecycled(holder: ViewHolder) {
            super.onViewRecycled(holder)
            holder.componentView?.removeObservers()
            holder.componentView = null

        }


    }

    private val mapProxyIndexComponentViewImpl = mutableMapOf<SKComponentViewProxy<*>, SKComponentView<*>>()

    private val adapter = Adapter()

    var items: List<SKComponentViewProxy<*>> = emptyList()
        set(newVal) {
            field.forEach { proxy ->
                if (!newVal.contains(proxy)) {
                    mapProxyIndexComponentViewImpl[proxy]?.removeObservers()
                    mapProxyIndexComponentViewImpl.remove(proxy)
                }
            }

            field = newVal
            adapter.notifyDataSetChanged()
        }


    init {
        recyclerView.layoutManager =
                if (nbColumns != null) {
                    GridLayoutManager(context, nbColumns, if (vertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, reverse)
                } else {
                    LinearLayoutManager(context, if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, reverse)
                }

        recyclerView.adapter = adapter
    }


    fun onItems(items: List<SKComponentViewProxy<*>>) {
        this.items = items
    }

    fun saveState(): Parcelable? {
        return recyclerView.layoutManager?.onSaveInstanceState()
    }

    fun restoreState(state: Parcelable) {
        recyclerView.layoutManager?.onRestoreInstanceState(state)
    }
}