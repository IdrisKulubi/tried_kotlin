package com.example.bakerysystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakerysystem.data.AppRepository
import com.example.bakerysystem.data.CartRepository
import com.example.bakerysystem.ui.AuthViewModel
import com.example.bakerysystem.ui.CartViewModel
import com.example.bakerysystem.ui.MenuViewModel

// Factory is needed because ViewModels take an argument (the Repository)
class AppViewModelFactory(
    private val appRepository: AppRepository,
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(appRepository) as T
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(appRepository) as T
            modelClass.isAssignableFrom(CartViewModel::class.java) -> CartViewModel(cartRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}