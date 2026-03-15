package com.example.coffeeshop.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.coffeeshop.data.DataRepository
import com.example.coffeeshop.model.UserModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel(private val repository: DataRepository):ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }

    // Check if user is already logged in
    fun isUserLoggedIn(): Boolean {
        return repository.getPreferenceBoolean("isLoggedIn")
    }

    // Save login state
    fun saveLoginState(isLoggedIn: Boolean) {
        repository.saveBoolean("isLoggedIn", isLoggedIn)
    }

    // Perform login and update state
    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveLoginState(true) // Save login status in repository
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
    }

}