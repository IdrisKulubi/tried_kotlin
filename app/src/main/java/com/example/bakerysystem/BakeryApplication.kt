package com.example.bakerysystem

import android.app.Application
import com.example.bakerysystem.data.AppDatabase
import com.example.bakerysystem.data.CartRepository

/**
 * Application class to initialize Room Database components only once
 * for the entire lifespan of the application.
 */
class BakeryApplication : Application() { // <--- This 'extends' android.app.Application

    // Lazy initialization of the database instance
    // This creates the database object only when it's first needed.
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    // Lazy initialization of the main AppRepository
    // The repository needs the DAO, which comes from the database.
    val appRepository by lazy {
        com.example.bakerysystem.data.AppRepository(database.appDao())
    }

    // Lazy initialization of the CartRepository
    val cartRepository by lazy {
        CartRepository(database.cartItemDao())
    }
}