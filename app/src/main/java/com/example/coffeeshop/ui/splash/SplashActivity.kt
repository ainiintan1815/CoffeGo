package com.example.coffeeshop.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.ui.intro.IntroActivity
import com.example.coffeeshop.activity.MainActivity
import com.example.coffeeshop.data.ViewModelFactory
import com.example.coffeeshop.databinding.ActivitySplashBinding
import com.example.coffeeshop.ui.bottom_nav.BottomNavActivity
import kotlin.getValue

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel: SplashViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }



    private val DURATION_TIME = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val isLoggedIn = viewModel.isUserLoggedIn()
            if (isLoggedIn) {
                //Kalau sudah login menuju halaman utama
                startActivity(Intent(this, BottomNavActivity::class.java))
                finish()  // Close WelcomeActivity
            } else {
                // Kalau belum ke intro dulu
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
            finish()
        }, DURATION_TIME)
    }

}