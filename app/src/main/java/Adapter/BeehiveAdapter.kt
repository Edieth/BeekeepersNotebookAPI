package Adapter

import Entity.Beehive
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.beekeepersnotebook.R

class BeehiveAdapter (
    private var items: MutableList<Beehive>,
    private val onItemClick: (Beehive) -> Unit,
    private val onItemLongClick: (Beehive) -> Unit
): RecyclerView.Adapter<BeehiveAdapter.BeehiveViewHolder>() {

        inner class BeehiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvBeehiveName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeehiveViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_beehive, parent, false)
            return BeehiveViewHolder(view)
        }

        override fun onBindViewHolder(holder: BeehiveViewHolder, position: Int) {
            val hive = items[position]
            holder.tvName.text = hive.Name
            holder.itemView.setOnClickListener { onItemClick(hive) }
            holder.itemView.setOnLongClickListener {
                onItemLongClick(hive)
                true
            }
        }

        override fun getItemCount(): Int = items.size

        fun setData(newItems: List<Beehive>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
}
