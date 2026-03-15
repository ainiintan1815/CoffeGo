package com.example.coffeeshop.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.di.Injection
import com.example.coffeeshop.ui.auth.AuthViewModel
import com.example.coffeeshop.ui.profile.ProfileViewModel
import com.example.coffeeshop.ui.setting.SettingViewModel
import com.example.coffeeshop.ui.splash.SplashViewModel

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository(context)

                instance ?: ViewModelFactory(repository).also { instance = it }
            }
        }
    }

}