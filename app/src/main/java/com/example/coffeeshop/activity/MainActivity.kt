package com.example.coffeeshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.R
import com.example.coffeeshop.ActivityProfile
import com.example.coffeeshop.adapter.CategoryAdapter
import com.example.coffeeshop.adapter.OffersAdapter
import com.example.coffeeshop.adapter.PopularAdapter
import com.example.coffeeshop.databinding.ActivityMainBinding
import com.example.coffeeshop.ui.cart.CartActivity
import com.example.coffeeshop.viewmodel.MainViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlin.jvm.java

private val Unit.menu_home: Any
    get() {
        TODO()
    }

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel   // <— TANPA viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init ViewModel cara klasik (nggak perlu activity-ktx)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // kalau desain bottom nav ada cutout
        runCatching { binding.bottomNavigation.background = null }

        setupRecyclerViews()
        observeData()
        setupBottomMenu()

        // Cegah crash kalau Firebase belum beres
        val app = FirebaseApp.initializeApp(this)
        if (app != null) {
            writeDataToFirebase()
        } else {
            Toast.makeText(
                this,
                "Firebase belum terpasang benar (cek google-services.json).",
                Toast.LENGTH_LONG
            ).show()
        }

        // load data dari ViewModel
        viewModel.loadCategory()
        viewModel.loadPopular()
        viewModel.loadOffer()
    }

    private fun setupBottomMenu() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Home activity atau main activity
                    true
                }
                R.id.menu_profile -> {
                    // Buka Profile Activity ketika Profile tombol diklik
                    val intent = Intent(this, ActivityProfile::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewOffer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeData() {
        binding.progressBarCategory.visibility = View.VISIBLE
        binding.progressBarPopular.visibility = View.VISIBLE
        binding.progressBarOffer.visibility = View.VISIBLE

        viewModel.category.observe(this) {
            binding.recyclerViewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        }
        viewModel.popular.observe(this) {
            binding.recyclerViewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        }
        viewModel.offer.observe(this) {
            binding.recyclerViewOffer.adapter = OffersAdapter(it)
            binding.progressBarOffer.visibility = View.GONE
        }
    }

    /** Tulis node test ke Realtime DB (aman kalau Firebase ready) */
    private fun writeDataToFirebase() {
        val db = FirebaseDatabase.getInstance(
            "https://coffego-890b3-default-rtdb.firebaseio.com"
        )
        db.getReference("dev_test").setValue(
            mapOf("status" to "ok", "time" to ServerValue.TIMESTAMP)
        ).addOnSuccessListener {
            Toast.makeText(this, "Write OK", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Write FAIL: ${e.message}", Toast.LENGTH_LONG).show()
        }

    }
}
