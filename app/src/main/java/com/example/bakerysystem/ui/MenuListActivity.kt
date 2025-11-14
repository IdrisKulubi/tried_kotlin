package com.example.bakerysystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.data.MenuItemEntity
import com.example.bakerysystem.databinding.ActivityMenuListBinding
import com.example.bakerysystem.ui.adapter.MenuItemAdapter

class MenuListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuListBinding
    private lateinit var itemAdapter: MenuItemAdapter

    private val menuViewModel: MenuViewModel by viewModels {
        val app = application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()

        // Button to navigate to Add Item screen
        binding.btnAdd.setOnClickListener {
            // Pass null/0 ID to indicate a new item creation
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = MenuItemAdapter(
            onItemClick = { item -> navigateToEdit(item) }, // Edit operation
            onDeleteClick = { item -> menuViewModel.deleteMenuItem(item) } // Delete operation (CRUD)
        )

        binding.rvMenuItems.apply {
            layoutManager = LinearLayoutManager(this@MenuListActivity)
            adapter = itemAdapter
        }
    }

    private fun setupObservers() {
        // Observe the LiveData list of menu items
        menuViewModel.menuItems.observe(this) { items ->
            // Update the RecyclerView automatically (LiveData/Flow concept)
            itemAdapter.submitList(items)
        }

        // Observe the status messages from the ViewModel
        menuViewModel.statusMessage.observe(this) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                menuViewModel.clearStatus() // Clear message after displaying
            }
        }
    }

    private fun navigateToEdit(item: MenuItemEntity) {
        val intent = Intent(this, AddItemActivity::class.java).apply {
            // Pass the item ID and data for editing
            putExtra("ITEM_ID", item.id)
            putExtra("ITEM_NAME", item.name)
            putExtra("ITEM_DESC", item.description)
            putExtra("ITEM_PRICE", item.price.toString())
            putExtra("ITEM_AVAIL", item.isAvailable)
        }
        startActivity(intent)
    }
}