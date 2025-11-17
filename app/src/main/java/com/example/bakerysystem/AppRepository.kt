package com.example.bakerysystem.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    // --- User Authentication ---

    suspend fun registerUser(user: UserEntity) {
        appDao.insertUser(user)
    }

    suspend fun loginUser(email: String): UserEntity? {
        return appDao.getUserByEmail(email)
    }

    // --- Menu Item CRUD ---

    val allMenuItems: Flow<List<MenuItemEntity>> = appDao.getAllMenuItems()

    suspend fun insertMenuItem(item: MenuItemEntity) {
        appDao.insertMenuItem(item)
    }

    suspend fun updateMenuItem(item: MenuItemEntity) {
        appDao.updateMenuItem(item)
    }

    suspend fun deleteMenuItem(item: MenuItemEntity) {
        appDao.deleteMenuItem(item)
    }
}