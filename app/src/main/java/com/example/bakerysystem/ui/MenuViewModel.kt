package com.example.bakerysystem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bakerysystem.data.AppRepository
import com.example.bakerysystem.data.MenuItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuViewModel(private val repository: AppRepository) : ViewModel() {

    val menuItems: LiveData<List<MenuItemEntity>> = repository.allMenuItems.asLiveData()

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    // ✅ Method for Activities to set status
    fun setStatus(message: String) {
        _statusMessage.postValue(message)
    }

    fun clearStatus() {
        _statusMessage.value = ""
    }

    fun saveMenuItem(item: MenuItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        if (item.name.isBlank() || item.description.isBlank()) {
            setStatus("Error: Name and description cannot be empty.")
            return@launch
        }
        repository.insertMenuItem(item)
        val action = if (item.id == 0) "added" else "updated"
        setStatus("Menu item $action successfully!")
    }

    fun deleteMenuItem(item: MenuItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMenuItem(item)
        setStatus("Menu item deleted.")
    }
}
