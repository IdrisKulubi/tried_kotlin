package com.example.bakerysystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    // Auto-generating unique ID for CRUD operations
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val isAvailable: Boolean = true,
    val imageUri: String
)