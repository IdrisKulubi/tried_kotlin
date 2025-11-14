package com.example.bakerysystem.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bakerysystem.R

class ProductsAdapter(private val products: List<MenuItemEntity>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    // Interface for click events
    interface OnItemClickListener {
        fun onProductClick(item: MenuItemEntity)
        fun onProductOptionsClick(item: MenuItemEntity, anchorView: View)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val optionsMenu: ImageView = itemView.findViewById(R.id.iv_options_menu)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductClick(products[position])
                }
            }
            optionsMenu.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductOptionsClick(products[position], it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productPrice.text = "$%.2f".format(product.price)
        if (!product.imageUri.isNullOrEmpty()) {
            holder.productImage.setImageURI(Uri.parse(product.imageUri))
        } else {
            holder.productImage.setImageResource(R.drawable.ic_placeholder) // Fallback placeholder
        }
    }

    override fun getItemCount() = products.size
}