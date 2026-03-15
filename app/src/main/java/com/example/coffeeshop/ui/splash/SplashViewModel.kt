package com.example.coffeeshop.ui.splash

import androidx.lifecycle.ViewModel
import com.example.coffeeshop.data.DataRepository

class SplashViewModel(private val repository: DataRepository):ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return repository.getPreferenceBoolean("isLoggedIn")
    }


}