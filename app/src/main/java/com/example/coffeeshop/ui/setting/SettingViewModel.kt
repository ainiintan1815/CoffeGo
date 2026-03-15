package com.example.coffeeshop.ui.setting

import androidx.lifecycle.ViewModel
import com.example.coffeeshop.data.DataRepository

class SettingViewModel(private val repository: DataRepository): ViewModel() {

    fun saveLoginState(isLoggedIn1: String, isLoggedIn: Boolean) {
        repository.saveBoolean("isLoggedIn", isLoggedIn)
    }

}