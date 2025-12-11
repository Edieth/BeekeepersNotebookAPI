package Adapter

import Entity.Zone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.beekeepersnotebook.R

class ZoneAdapter(
    private var items: MutableList<Zone>,
    private val onItemClick: (Zone) -> Unit,
    private val onItemLongClick: (Zone) -> Unit
) : RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder>() {

    inner class ZoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvZoneName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_zone, parent, false)
        return ZoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: ZoneViewHolder, position: Int) {
        val zone = items[position]
        holder.tvName.text = zone.Name
        holder.itemView.setOnClickListener { onItemClick(zone) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(zone)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun setData(newItems: List<Zone>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}