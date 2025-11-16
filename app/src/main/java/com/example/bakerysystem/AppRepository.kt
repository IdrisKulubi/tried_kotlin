package com.example.bakerysystem.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(private val appDao: AppDao, private val context: Context) {

    private val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val CURRENT_USER_ID_KEY = "current_user_id"

    // --- User Authentication ---

    suspend fun registerUser(user: UserEntity) {
        appDao.insertUser(user)
    }

    suspend fun loginUser(email: String): UserEntity? {
        return appDao.getUserByEmail(email)
    }

    fun saveLoggedInUserId(userId: Int) {
        preferences.edit().putInt(CURRENT_USER_ID_KEY, userId).apply()
    }

    fun getLoggedInUserId(): Int {
        return preferences.getInt(CURRENT_USER_ID_KEY, -1) // -1 for no user logged in
    }

    fun clearLoggedInUserId() {
        preferences.edit().remove(CURRENT_USER_ID_KEY).apply()
    }

    fun getLoggedInUser(): Flow<UserEntity?> {
        val userId = getLoggedInUserId()
        return if (userId != -1) {
            appDao.getUserById(userId).map { user ->
                // Handle case where user might have been deleted, but ID still in prefs
                if (user == null) clearLoggedInUserId()
                user
            }
        } else {
            // If no user is logged in, return a flow that emits null
            kotlinx.coroutines.flow.flowOf(null)
        }
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