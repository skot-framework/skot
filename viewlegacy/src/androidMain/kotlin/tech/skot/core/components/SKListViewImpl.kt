package tech.skot.core.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SKListViewImpl(activity: SKActivity, fragment: Fragment?, private val recyclerView: RecyclerView) : ComponentViewImpl<RecyclerView>(activity, fragment, recyclerView) {



    inner class ViewHolder(idLayout:Int, parent:ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(idLayout, parent, false)) {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
    }

    inner class Adapter:RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(viewType, parent)

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            items[position].let { proxy ->
                proxy.bindToItemView(activity, fragment, holder.layoutInflater, holder.itemView)
            }
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