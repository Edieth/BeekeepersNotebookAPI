package Adapter

import Entity.InventoryItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.beekeepersnotebook.R

class InventoryAdapter ( private var items: MutableList<InventoryItem>,
                         private val onItemClick: (InventoryItem) -> Unit,
                         private val onItemLongClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    inner class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvTotals: TextView = itemView.findViewById(R.id.tvItemTotals)
        val tvDataType: TextView = itemView.findViewById(R.id.tvItemDataType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.Name
        holder.tvDataType.text = item.InventoryDataType
        holder.tvTotals.text =
            "Material: ${item.Name}, Cantidad: ${item.InventoryTotalQuantity} Tipo de dato: ${item.InventoryDataType}"

        holder.itemView.setOnClickListener { onItemClick(item) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun setData(newItems: List<InventoryItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}