package com.example.bakerysystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "cart_items",
    foreignKeys = [
        ForeignKey(entity = MenuItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["menuItemId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val menuItemId: Int,
    val name: String,
    val price: Double,
    var quantity: Int
)