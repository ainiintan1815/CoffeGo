package com.example.coffeeshop.ui.profile

import androidx.lifecycle.ViewModel
import com.example.coffeeshop.data.DataRepository

class ProfileViewModel(private val repository: DataRepository):ViewModel() {

    fun saveLoginState(isLoggedIn1: String, isLoggedIn: Boolean) {
        repository.saveBoolean("isLoggedIn", isLoggedIn)
    }

}