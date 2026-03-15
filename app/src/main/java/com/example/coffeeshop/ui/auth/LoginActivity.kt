package com.example.coffeeshop.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.activity.MainActivity
import com.example.coffeeshop.data.ViewModelFactory
import com.example.coffeeshop.databinding.ActivityLoginBinding
import com.example.coffeeshop.model.UserModel
import com.example.coffeeshop.ui.bottom_nav.BottomNavActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.getValue

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var realtimeDb: DatabaseReference
    private lateinit var user: UserModel

    private val loginViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init Realtime DB
        realtimeDb = FirebaseDatabase.getInstance().getReference("User")

        binding.buatAkun.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        setupView()
        setupLoginObserver()

        if (loginViewModel.isUserLoggedIn()) {
            startAppropriateActivity()
        } else {
            setupLoginButton()
        }
    }

    private fun setupLoginButton() {
        binding.loginBtn.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty()) {
                binding.edtEmail.error = "Masukkan email yang benar"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = "Masukkan kata sandi yang benar"
                return@setOnClickListener
            }

            binding.progressCircular.root.visibility = View.VISIBLE

            loginViewModel.login(email, password) { success ->
                binding.progressCircular.root.visibility = View.GONE
                if (success) {
                    startAppropriateActivity()
                } else {
                    Toast.makeText(this, "Login gagal, silahkan coba lagi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Cek user di Realtime Database
     */
    private fun startAppropriateActivity() {
        val userId = auth.currentUser?.uid ?: return

        realtimeDb.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // User ditemukan → lanjut masuk ke aplikasinya
                startActivity(Intent(this, BottomNavActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Data user tidak ditemukan", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal memuat data user", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupLoginObserver() {
        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
