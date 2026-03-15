package com.example.coffeeshop.data

import com.example.coffeeshop.data.preference.UserPreference
import com.example.coffeeshop.model.UserModel
import kotlinx.coroutines.flow.Flow

class DataRepository(
    private val preference: UserPreference,
    ) {

    fun getPreferenceBoolean(key: String): Boolean {
        return preference.getPreferenceBoolean(key)
    }

    // Save boolean preference
    fun saveBoolean(key: String, value: Boolean) {
        preference.saveBoolean(key, value)
    }

    fun getUser(): Flow<UserModel> {
        return preference.getUser()
    }



        }