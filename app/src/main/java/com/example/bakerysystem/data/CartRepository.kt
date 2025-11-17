package com.example.bakerysystem.data

import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartItemDao: CartItemDao) {

    val allCartItems: Flow<List<CartItemEntity>> = cartItemDao.getAllCartItems()
    val totalPrice: Flow<Double?> = cartItemDao.getTotalPrice()
    val cartItemCount: Flow<Int> = cartItemDao.getCartItemCount()

    suspend fun insert(cartItem: CartItemEntity) {
        cartItemDao.insert(cartItem)
    }

    suspend fun update(cartItem: CartItemEntity) {
        cartItemDao.update(cartItem)
    }

    suspend fun delete(cartItem: CartItemEntity) {
        cartItemDao.delete(cartItem)
    }

    suspend fun deleteAllCartItems() {
        cartItemDao.deleteAllCartItems()
    }

    suspend fun getCartItemByMenuItemId(menuItemId: Int): CartItemEntity? {
        return cartItemDao.getCartItemByMenuItemId(menuItemId)
    }
}