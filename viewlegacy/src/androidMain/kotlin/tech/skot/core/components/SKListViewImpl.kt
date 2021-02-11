package tech.skot.core.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.skot.core.SKLog
import java.lang.IllegalStateException

class SKListViewImpl(activity: SKActivity, fragment: Fragment?, private val recyclerView: RecyclerView) : ComponentViewImpl<RecyclerView>(activity, fragment, recyclerView) {



    inner class ViewHolder(idLayout:Int, parent:ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(idLayout, parent, false)) {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)

        var componentViewImpl:ComponentViewImpl<*>? = null

    }

    inner class Adapter:RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(viewType, parent)

        override fun getItemViewType(position: Int) = items[position].layoutId ?: throw IllegalStateException("${items[position]::class.simpleName} can't be in a recyclerview")

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            SKLog.d("${holder.hashCode()} onBindViewHolder")

            items[position].let { proxy ->
                holder.componentViewImpl = proxy.bindToItemView(activity, fragment, holder.itemView)
            }
        }

        override fun onViewRecycled(holder: ViewHolder) {
            super.onViewRecycled(holder)
            SKLog.d("${holder.hashCode()} onViewRecycled")
            holder.componentViewImpl?.removeObservers()
            holder.componentViewImpl= null

        }

        override fun onViewAttachedToWindow(holder: ViewHolder) {
            super.onViewAttachedToWindow(holder)
            SKLog.d("${holder.hashCode()} onViewAttachedToWindow")
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            SKLog.d("${holder.hashCode()} onViewDetachedFromWindow")
        }



    }

    private val adapter = Adapter()

    var items:List<ComponentViewProxy<*>> = emptyList()
    set(newVal) {

        field = newVal
        adapter.notifyDataSetChanged()
    }


    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    fun onItems(items:List<ComponentViewProxy<*>>) {
        this.items = items
    }
}