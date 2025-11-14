package com.example.bakerysystem.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bakerysystem.databinding.ItemMenuItemBinding

/**
 * RecyclerView Adapter for displaying menu items.
 * Uses DiffUtil for efficient list updates.
 */
class MenuItemAdapter(
    private val onItemClick: (MenuItemEntity) -> Unit,
    private val onDeleteClick: (MenuItemEntity) -> Unit
) : ListAdapter<MenuItemEntity, MenuItemAdapter.ItemViewHolder>(MenuItemDiffCallback()) {

    // Inner class for the ViewHolder
    inner class ItemViewHolder(private val binding: ItemMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItemEntity) {
            binding.apply {
                tvItemName.text = item.name
                tvItemPrice.text = String.format("$%.2f", item.price)
                tvItemDescription.text = item.description
                tvAvailability.text = if (item.isAvailable) "Available" else "Sold Out"
                tvAvailability.setTextColor(if (item.isAvailable) 0xFF4CAF50.toInt() else 0xFFF44336.toInt())

                // Click listener to edit the item
                root.setOnClickListener {
                    onItemClick(item)
                }

                // Delete button listener
                btnDelete.setOnClickListener {
                    onDeleteClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

// DiffUtil to handle efficient list comparison
class MenuItemDiffCallback : DiffUtil.ItemCallback<MenuItemEntity>() {
    override fun areItemsTheSame(oldItem: MenuItemEntity, newItem: MenuItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MenuItemEntity, newItem: MenuItemEntity): Boolean {
        return oldItem == newItem
    }
}