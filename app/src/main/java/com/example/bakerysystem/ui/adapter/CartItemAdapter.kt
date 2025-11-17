package com.example.bakerysystem.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bakerysystem.data.CartItemEntity
import com.example.bakerysystem.databinding.ItemCartItemBinding

class CartItemAdapter(
    private val onIncreaseQuantity: (CartItemEntity) -> Unit,
    private val onDecreaseQuantity: (CartItemEntity) -> Unit,
    private val onRemoveItem: (CartItemEntity) -> Unit
) : ListAdapter<CartItemEntity, CartItemAdapter.ItemViewHolder>(CartItemDiffCallback()) {

    inner class ItemViewHolder(private val binding: ItemCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItemEntity) {
            binding.apply {
                tvCartItemName.text = cartItem.name
                tvCartItemPrice.text = String.format("$%.2f", cartItem.price * cartItem.quantity)
                tvCartItemQuantity.text = "Quantity: ${cartItem.quantity}"

                btnIncreaseQuantity.setOnClickListener { onIncreaseQuantity(cartItem) }
                btnDecreaseQuantity.setOnClickListener { onDecreaseQuantity(cartItem) }
                btnRemoveItem.setOnClickListener { onRemoveItem(cartItem) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCartItemBinding.inflate(
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

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItemEntity>() {
    override fun areItemsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
        return oldItem == newItem
    }
}