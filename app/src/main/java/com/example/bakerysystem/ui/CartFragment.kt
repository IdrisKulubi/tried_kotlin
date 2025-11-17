package com.example.bakerysystem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.data.CartItemEntity
import com.example.bakerysystem.databinding.FragmentCartBinding
import com.example.bakerysystem.ui.adapter.CartItemAdapter

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartItemAdapter: CartItemAdapter

    private val cartViewModel: CartViewModel by activityViewModels {
        val app = requireActivity().application as BakeryApplication
        AppViewModelFactory(app.appRepository, app.cartRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        binding.btnCheckout.setOnClickListener {
            // Check if cart is not empty before proceeding
            if (cartViewModel.allCartItems.value?.isNotEmpty() == true) {
                cartViewModel.deleteAllCartItems()
                Toast.makeText(requireContext(), "Purchase successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        cartItemAdapter = CartItemAdapter(
            onIncreaseQuantity = { cartItem -> updateCartItemQuantity(cartItem, 1) },
            onDecreaseQuantity = { cartItem -> updateCartItemQuantity(cartItem, -1) },
            onRemoveItem = { cartItem -> cartViewModel.deleteCartItem(cartItem) }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartItemAdapter
        }
    }

    private fun setupObservers() {
        cartViewModel.allCartItems.observe(viewLifecycleOwner) { items ->
            cartItemAdapter.submitList(items)
            binding.rvCartItems.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
            // Disable checkout button if cart is empty
            binding.btnCheckout.isEnabled = items.isNotEmpty()
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            binding.tvTotalPrice.text = String.format("Total: $%.2f", total ?: 0.0)
        }
    }

    private fun updateCartItemQuantity(cartItem: CartItemEntity, change: Int) {
        val newQuantity = cartItem.quantity + change
        if (newQuantity > 0) {
            cartViewModel.updateCartItem(cartItem.copy(quantity = newQuantity))
        } else {
            // If quantity drops to 0 or less, remove item from cart
            cartViewModel.deleteCartItem(cartItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}