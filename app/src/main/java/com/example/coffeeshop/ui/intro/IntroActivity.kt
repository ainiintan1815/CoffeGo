package com.example.coffeeshop.ui.intro

import android.content.Intent
import android.os.Bundle
import com.example.coffeeshop.activity.BaseActivity
import com.example.coffeeshop.databinding.ActivityIntroBinding
import com.example.coffeeshop.ui.auth.LoginActivity

class IntroActivity : BaseActivity() {

    private val binding: ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            // finish() // opsional: tutup layar intro
        }
    }
}