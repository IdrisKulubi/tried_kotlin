package com.example.bakerysystem.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- User Operations (Registration/Login) ---

    // OnConflictStrategy.ABORT prevents duplicate emails
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<UserEntity?>

    // --- Menu Item Operations (CRUD) ---

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMenuItem(item: MenuItemEntity)

    @Update
    suspend fun updateMenuItem(item: MenuItemEntity)

    @Delete
    suspend fun deleteMenuItem(item: MenuItemEntity)

    // Flow for real-time menu updates (LiveData concept)
    @Query("SELECT * FROM menu_items ORDER BY name ASC")
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>
}