package com.example.bakerysystem.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Get the ViewModel using the custom Factory
    private val authViewModel: AuthViewModel by viewModels {
        val app = application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Implement Data Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Observe Authentication Status (LiveData concept)
        authViewModel.authStatus.observe(this) { status ->
            if (status == "Login Success") {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                // Navigate to Menu List
                startActivity(Intent(this, MenuListActivity::class.java))
                finish()
            } else if (status.startsWith("Error")) {
                Toast.makeText(this, status, Toast.LENGTH_LONG).show()
            }
        }

        // 2. Handle Login Button Click
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            authViewModel.login(email, password)
        }

        // 3. Handle Navigation to Registration
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}