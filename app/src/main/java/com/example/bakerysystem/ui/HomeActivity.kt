package com.example.bakerysystem.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bakerysystem.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val productsFragment = ProductsFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = homeFragment
                R.id.nav_products -> selectedFragment = productsFragment
                R.id.nav_account -> selectedFragment = accountFragment
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            }
            true
        }

        // Set default fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
    }
}