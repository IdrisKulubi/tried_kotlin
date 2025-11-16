package com.example.bakerysystem.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.data.CartItemEntity
import com.example.bakerysystem.data.MenuItemEntity
import com.example.bakerysystem.databinding.FragmentMenuListBinding
import com.example.bakerysystem.ui.adapter.MenuItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuListFragment : Fragment() {

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: MenuItemAdapter

    private val menuViewModel: MenuViewModel by viewModels {
        val app = requireActivity().application as BakeryApplication
        AppViewModelFactory(app.appRepository, app.cartRepository)
    }

    private val cartViewModel: CartViewModel by activityViewModels {
        val app = requireActivity().application as BakeryApplication
        AppViewModelFactory(app.appRepository, app.cartRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        binding.btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddItemActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = MenuItemAdapter(
            onItemClick = { item -> navigateToEdit(item) },
            onDeleteClick = { item -> menuViewModel.deleteMenuItem(item) },
            onAddToCartClick = { item -> addToCart(item) }
        )

        binding.rvMenuItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemAdapter
        }
    }

    private fun setupObservers() {
        menuViewModel.menuItems.observe(viewLifecycleOwner) { items ->
            itemAdapter.submitList(items)
        }

        menuViewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                menuViewModel.clearStatus()
            }
        }
    }

    private fun navigateToEdit(item: MenuItemEntity) {
        val intent = Intent(requireContext(), AddItemActivity::class.java).apply {
            putExtra("ITEM_ID", item.id)
            putExtra("ITEM_NAME", item.name)
            putExtra("ITEM_DESC", item.description)
            putExtra("ITEM_PRICE", item.price.toString())
            putExtra("ITEM_AVAIL", item.isAvailable)
        }
        startActivity(intent)
    }

    private fun addToCart(menuItem: MenuItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingCartItem = cartViewModel.getCartItemByMenuItemId(menuItem.id)
            if (existingCartItem == null) {
                // Item not in cart, add new
                val newCartItem = CartItemEntity(
                    menuItemId = menuItem.id,
                    name = menuItem.name,
                    price = menuItem.price,
                    quantity = 1
                )
                cartViewModel.insertCartItem(newCartItem)
                // Post a message back to main thread for Toast
                activity?.runOnUiThread { Toast.makeText(requireContext(), "${menuItem.name} added to cart", Toast.LENGTH_SHORT).show() }
            } else {
                // Item already in cart, update quantity
                existingCartItem.quantity++
                cartViewModel.updateCartItem(existingCartItem)
                // Post a message back to main thread for Toast
                activity?.runOnUiThread { Toast.makeText(requireContext(), "${menuItem.name} quantity updated in cart", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}