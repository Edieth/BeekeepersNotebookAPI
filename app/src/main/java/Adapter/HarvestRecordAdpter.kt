package Adapter

import Entity.HarvestRecord
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.beekeepersnotebook.R

class HarvestRecordAdpter (
    private var items: MutableList<HarvestRecord>,
    private val onItemClick: (HarvestRecord) -> Unit,
    private val onItemLongClick: (HarvestRecord) -> Unit
    ) : RecyclerView.Adapter<HarvestRecordAdpter.HarvestViewHolder>() {

        inner class HarvestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvInfo: TextView = itemView.findViewById(R.id.tvHarvestInfo)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HarvestViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_harvest, parent, false)
            return HarvestViewHolder(view)
        }

    override fun onBindViewHolder(holder: HarvestViewHolder, position: Int) {
        val h = items[position]
        holder.tvInfo.text =
            "Fecha: ${h.DateHarvestHoney} - Marcos: ${h.HoneyFramesHarvest} " +
                    "- Kg bruto: ${h.HoneyAmountKgHarvest} - Kg neto: ${h.HoneyAmountKgNetaHarvest}"
        holder.itemView.setOnClickListener { onItemClick(h) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(h)
            true
        }
    }

        override fun getItemCount(): Int = items.size

        fun setData(newItems: List<HarvestRecord>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
}