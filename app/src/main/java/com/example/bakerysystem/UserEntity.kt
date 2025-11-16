package com.example.bakerysystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    // Primary key for authenticationn
    @PrimaryKey val email: String,
    val username: String,
    val passwordHash: String // Stored as plain text for assignment simplicity
)