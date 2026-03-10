package com.store.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.store.app.R
import com.store.app.data.local.entity.DealEntity

class DealAdapter(
    private val onItemClick: (DealEntity) -> Unit
) : ListAdapter<DealEntity, DealAdapter.DealViewHolder>(DealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deal_card, parent, false)
        return DealViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DealViewHolder(
        itemView: View,
        private val onItemClick: (DealEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val dealTitle: TextView = itemView.findViewById(R.id.dealTitle)
        private val dealDescription: TextView = itemView.findViewById(R.id.dealDescription)
        private val originalPrice: TextView = itemView.findViewById(R.id.originalPrice)
        private val discountedPrice: TextView = itemView.findViewById(R.id.discountedPrice)
        private val discountBadge: TextView = itemView.findViewById(R.id.discountBadge)
        private val dealImage: ImageView = itemView.findViewById(R.id.dealImage)

        fun bind(deal: DealEntity) {
            dealTitle.text = deal.title
            dealDescription.text = deal.description
            
            // Strike through original price
            originalPrice.paintFlags = originalPrice.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            originalPrice.text = String.format("$%.2f", deal.originalPrice)
            
            discountedPrice.text = String.format("$%.2f", deal.discountedPrice)
            discountBadge.text = String.format("%d%% OFF", deal.discountPercentage)
            
            itemView.setOnClickListener { onItemClick(deal) }
        }
    }

    class DealDiffCallback : DiffUtil.ItemCallback<DealEntity>() {
        override fun areItemsTheSame(oldItem: DealEntity, newItem: DealEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DealEntity, newItem: DealEntity): Boolean {
            return oldItem == newItem
        }
    }
}
