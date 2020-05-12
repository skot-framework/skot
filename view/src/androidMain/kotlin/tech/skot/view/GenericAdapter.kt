package tech.skot.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


open class GenericAdapter(vararg possibleDefs: ItemDef) : RecyclerView.Adapter<GenericAdapter.Holder>() {


    var items: List<Item> = emptyList()
        set(value) {
            if (value != field) {
                if (value.isEmpty()) {
                    notifyDataSetChanged()
                } else {
                    val diffCallBack = DiffCallBack(field, value)
                    DiffUtil.calculateDiff(diffCallBack, true).dispatchUpdatesTo(this@GenericAdapter)
                }
            }
            field = value
        }


    class DiffCallBack(private val oldList: List<Item>, private val newList: List<Item>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.def == newItem.def && oldItem.computeItemId() == newItem.computeItemId()
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return if (oldItem is ValorisedItem<*>) {
                oldItem.data == (newItem as ValorisedItem<*>).data
            } else {
                true
            }
        }

    }

    interface Item {
        val def: ItemDef
        fun bind(itemView: View)
        fun sameItem(otherItem: Item): Boolean
        fun computeItemId(): Any?
    }

    interface ItemDef {
        val idLayout: Int
        val initialize: (View.() -> Unit)?
    }

    class FixItemDef(override val idLayout: Int, override val initialize: (View.() -> Unit)? = null) : ItemDef

    class FixItem(override val def: FixItemDef) : Item {
        override fun bind(itemView: View) {
        }

        override fun sameItem(otherItem: Item) = this == otherItem
        override fun computeItemId() = null
    }

    class WithDataItemDef<D>(override val idLayout: Int, override val initialize: (View.() -> Unit)? = null, val buildOnSwipe: ((data: D) -> (() -> Unit)?)? = null, val computeId: ((data: D) -> Any)? = null, val bindData: View.(data: D) -> Unit) : ItemDef

    class ValorisedItem<D>(override val def: WithDataItemDef<D>, val data: D) : Item {

        override fun sameItem(otherItem: Item) =
                def == otherItem.def &&
                        ((def.computeId != null && computeItemId() == otherItem.computeItemId())
                                ||
                                def.computeId == null && (data == (otherItem as ValorisedItem<*>).data)
                                )

        override fun computeItemId() =
                def.computeId?.invoke(data)


        var onSwipe: (() -> Unit)? = null

        override fun bind(itemView: View) {
            def.bindData.invoke(itemView, data)
            onSwipe = def.buildOnSwipe?.invoke(data)
        }
    }


    private val mapTypeItemDef: Map<Int, ItemDef> = possibleDefs.mapIndexed { index, def -> index to def }.toMap()
    private val mapItemDefType: Map<ItemDef, Int> = possibleDefs.mapIndexed { index, def -> def to index }.toMap()


    override fun getItemViewType(position: Int) = mapItemDefType[items[position].def]!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(mapTypeItemDef[viewType]!!, parent)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        item.bind(holder.itemView)
    }


    inner class Holder(val item: ItemDef, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(item.idLayout, parent, false)) {
        init {
            item.initialize?.invoke(itemView)
        }

        var id: String? = null
    }
}
