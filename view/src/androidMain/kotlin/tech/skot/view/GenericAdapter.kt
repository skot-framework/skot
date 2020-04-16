package tech.skot.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


open class GenericAdapter(val def: () -> List<Item>, possibleDefs: List<ItemDef>) : RecyclerView.Adapter<GenericAdapter.Holder>() {


    interface Item {
        val def: ItemDef
        fun bind(itemView: View)
    }

    interface ItemDef {
        val idLayout: Int
        val initialize: (View.() -> Unit)?
    }

    class FixItemDef(override val idLayout: Int, override val initialize: (View.() -> Unit)? = null) : ItemDef

    class FixItem(override val def: FixItemDef) : Item {
        override fun bind(itemView: View) {
        }
    }

    class WithDataItemDef<D>(override val idLayout: Int, override val initialize: (View.() -> Unit)? = null, val buildOnSwipe: ((data: D) -> (() -> Unit)?)? = null, val bindData: View.(data: D) -> Unit) : ItemDef

    class ValorisedItem<D>(override val def: WithDataItemDef<D>, val data: D) : Item {

        var onSwipe: (() -> Unit)? = null

        override fun bind(itemView: View) {
            def.bindData.invoke(itemView, data)
            onSwipe = def.buildOnSwipe?.invoke(data)
        }
    }


    val mapTypeItemDef: Map<Int, ItemDef> = possibleDefs.mapIndexed { index, def -> index to def }.toMap()
    val mapItemDefType: Map<ItemDef, Int> = possibleDefs.mapIndexed { index, def -> def to index }.toMap()

    override fun getItemViewType(position: Int) = mapItemDefType[def()[position].def]!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(mapTypeItemDef[viewType]!!, parent)

    override fun getItemCount() = def().size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = def()[position]
        item.bind(holder.itemView)
    }


    inner class Holder(val item: ItemDef, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(item.idLayout, parent, false)) {
        init {
            item.initialize?.invoke(itemView)
        }

        var id: String? = null
    }
}
