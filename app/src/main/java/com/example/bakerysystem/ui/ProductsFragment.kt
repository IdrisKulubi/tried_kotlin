package com.example.bakerysystem.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.R
import com.example.bakerysystem.data.MenuItemEntity // Added this import
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsFragment : Fragment(), ProductsAdapter.OnItemClickListener {

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var emptyStateTextView: TextView
    private lateinit var fabAddProduct: FloatingActionButton

    private val menuViewModel: MenuViewModel by viewModels {
        val app = requireActivity().application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products, container, false)

        productsRecyclerView = view.findViewById(R.id.products_recycler_view)
        emptyStateTextView = view.findViewById(R.id.tv_empty_state)
        fabAddProduct = view.findViewById(R.id.fab_add_product)

        // Ensure the FAB is always visible
        fabAddProduct.visibility = View.VISIBLE

        productsRecyclerView.layoutManager = LinearLayoutManager(context)

        fabAddProduct.setOnClickListener { 
            val intent = Intent(requireContext(), AddItemActivity::class.java)
            startActivity(intent)
        }

        menuViewModel.menuItems.observe(viewLifecycleOwner) { items: List<MenuItemEntity> ->
            if (items.isEmpty()) {
                productsRecyclerView.visibility = View.GONE
                emptyStateTextView.visibility = View.VISIBLE
            } else {
                productsRecyclerView.visibility = View.VISIBLE
                emptyStateTextView.visibility = View.GONE
                productsRecyclerView.adapter = ProductsAdapter(items, this)
            }
        }

        menuViewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                menuViewModel.clearStatus()
            }
        }

        return view
    }

    override fun onProductClick(item: MenuItemEntity) {
        // Handle product click (e.g., navigate to detail screen)
        Toast.makeText(requireContext(), "Clicked on: ${item.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onProductOptionsClick(item: MenuItemEntity, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.menu_item_options, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(requireContext(), AddItemActivity::class.java).apply {
                        putExtra("ITEM_ID", item.id)
                        putExtra("ITEM_NAME", item.name)
                        putExtra("ITEM_DESC", item.description)
                        putExtra("ITEM_PRICE", item.price.toString())
                        putExtra("ITEM_AVAIL", item.isAvailable)
                        putExtra("ITEM_IMAGE_URI", item.imageUri)
                    }
                    startActivity(intent)
                    true
                }
                R.id.action_delete -> {
                    menuViewModel.deleteMenuItem(item)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}