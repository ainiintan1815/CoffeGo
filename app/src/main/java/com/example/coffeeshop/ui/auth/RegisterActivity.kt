package com.example.coffeeshop.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.databinding.ActivityLoginBinding
import com.example.coffeeshop.databinding.ActivityRegisterBinding
import com.example.coffeeshop.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var realtimeDb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        realtimeDb = FirebaseDatabase.getInstance().getReference("User")

        binding.toLogin.setOnClickListener {
            finish()
        }

        binding.registerBtn.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPassword.text.toString()
            val confirmPass = binding.edtUlangPassword.text.toString()

            when {
                email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Invalid atau Email kosong", Toast.LENGTH_LONG).show()
                }
                pass.isEmpty() || confirmPass.isEmpty() -> {
                    Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_LONG).show()
                }
                pass.length < 8 -> {
                    Toast.makeText(this, "Password minimal 8 karakter", Toast.LENGTH_LONG).show()
                }
                pass != confirmPass -> {
                    Toast.makeText(this, "Password tidak sama", Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressCircular.root.visibility = View.VISIBLE

                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            binding.progressCircular.root.visibility = View.GONE

                            if (task.isSuccessful) {

                                val userId = firebaseAuth.currentUser!!.uid
                                val photoUrl =
                                    "https://ui-avatars.com/api/?background=8692F7&color=fff&size=100&rounded=true&name=$email"

                                val data = User(
                                    username = username,
                                    email = email,
                                    fotoProfil = photoUrl
                                )

                                // Simpan ke Realtime DB
                                realtimeDb.child(userId).setValue(data)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Sign Up Sukses", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                                    }

                            } else {
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
    }
}