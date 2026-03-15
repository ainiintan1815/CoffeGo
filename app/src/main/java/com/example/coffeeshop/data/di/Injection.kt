package com.example.coffeeshop.data.di

import android.content.Context
import com.example.coffeeshop.data.DataRepository
import com.example.coffeeshop.data.preference.UserPreference
import com.example.coffeeshop.data.preference.dataStore

object Injection {

    fun provideRepository(context: Context): DataRepository {
        val pref = UserPreference.getInstance(context, context.dataStore)



        return DataRepository( pref)

    }

}