package com.example.bakerysystem.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.databinding.ActivityRegisterBinding
import com.example.bakerysystem.AppViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
f
    private val authViewModel: AuthViewModel by viewModels {
        val app = application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe Registration Status
        authViewModel.authStatus.observe(this) { status ->
            if (status == "Registration Success") {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show()
                finish()
            } else if (status.startsWith("Error")) {
                Toast.makeText(this, status, Toast.LENGTH_LONG).show()
            }
        }

        // Handle Register Button Click
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            authViewModel.register(email, username, password)
        }

        // Back to login
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}
