package com.example.bakerysystem.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakerysystem.data.AppRepository
import com.example.bakerysystem.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AppRepository) : ViewModel() {

    // Status LiveData to inform the UI about success/failure (LiveData concept)
    private val _authStatus = MutableLiveData<String>()
    val authStatus: LiveData<String> = _authStatus

    fun register(email: String, username: String, passwordRaw: String) = viewModelScope.launch(Dispatchers.IO) {
        if (email.isBlank() || username.isBlank() || passwordRaw.isBlank()) {
            _authStatus.postValue("Error: All fields are required.")
            return@launch
        }

        val newUser = UserEntity(
            email = email.trim(),
            username = username.trim(),
            passwordHash = passwordRaw.trim()
        )

        Log.d("REGISTER_DEBUG", "Registering user: $newUser") // <- debug

        try {
            repository.registerUser(newUser)
            _authStatus.postValue("Registration Success")
        } catch (e: Exception) {
            Log.e("REGISTER_DEBUG", "Error inserting user", e)
            _authStatus.postValue("Error: User already exists or database issue.")
        }
    }

    fun login(email: String, passwordRaw: String) = viewModelScope.launch(Dispatchers.IO) {
        if (email.isBlank() || passwordRaw.isBlank()) {
            _authStatus.postValue("Error: Email and password are required.")
            return@launch
        }

        val user = repository.loginUser(email.trim())

        // Debug logs to check what is happening
        Log.d("LOGIN_DEBUG", "User from DB: $user")
        Log.d("LOGIN_DEBUG", "Password entered: '$passwordRaw'")
        Log.d("LOGIN_DEBUG", "Password stored: '${user?.passwordHash}'")

        if (user != null && user.passwordHash == passwordRaw) {
            _authStatus.postValue("Login Success")
        } else {
            _authStatus.postValue("Error: Invalid email or password.")
        }
    }

}