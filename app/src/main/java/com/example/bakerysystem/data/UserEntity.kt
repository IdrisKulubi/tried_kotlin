package com.example.bakerysystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val username: String,
    val passwordHash: String // Stored as plain text for assignment simplicity
)