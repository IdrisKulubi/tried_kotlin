package com.example.bakerysystem.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.R

class WelcomeActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        val app = application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Handler().postDelayed({
            val nextActivity = if (authViewModel.repository.getLoggedInUserId() != -1) {
                HomeActivity::class.java
            } else {
                LoginActivity::class.java
            }
            startActivity(Intent(this, nextActivity))
            finish()
        }, 3000) // 3 seconds delay
    }
}