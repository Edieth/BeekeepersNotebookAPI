package Adapter

import Entity.Queen
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.beekeepersnotebook.R

class QueenAdapter (
    private var items: MutableList<Queen>,
    private val onItemClick: (Queen) -> Unit,
    private val onItemLongClick: (Queen) -> Unit
    ) : RecyclerView.Adapter<QueenAdapter.QueenViewHolder>() {

        inner class QueenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvType: TextView = itemView.findViewById(R.id.tvQueenType)
            val tvAge: TextView = itemView.findViewById(R.id.tvQueenAge)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueenViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_queen, parent, false)
            return QueenViewHolder(view)
        }

        override fun onBindViewHolder(holder: QueenViewHolder, position: Int) {
            val q = items[position]
            holder.tvType.text = q.Type
            holder.tvAge.text = "Edad: ${q.AgeText()}"
            holder.itemView.setOnClickListener { onItemClick(q) }
            holder.itemView.setOnLongClickListener {
                onItemLongClick(q)
                true
            }
        }

        override fun getItemCount(): Int = items.size

        fun setData(newItems: List<Queen>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
}