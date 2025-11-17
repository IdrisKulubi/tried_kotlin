package com.example.bakerysystem.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bakerysystem.data.CartItemEntity
import com.example.bakerysystem.data.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    val allCartItems = cartRepository.allCartItems.asLiveData()
    val totalPrice = cartRepository.totalPrice.asLiveData()
    val cartItemCount = cartRepository.cartItemCount.asLiveData()

    fun insertCartItem(cartItem: CartItemEntity) = viewModelScope.launch {
        cartRepository.insert(cartItem)
    }

    fun updateCartItem(cartItem: CartItemEntity) = viewModelScope.launch {
        cartRepository.update(cartItem)
    }

    fun deleteCartItem(cartItem: CartItemEntity) = viewModelScope.launch {
        cartRepository.delete(cartItem)
    }

    fun deleteAllCartItems() = viewModelScope.launch {
        cartRepository.deleteAllCartItems()
    }

    suspend fun getCartItemByMenuItemId(menuItemId: Int): CartItemEntity? {
        return cartRepository.getCartItemByMenuItemId(menuItemId)
    }
}