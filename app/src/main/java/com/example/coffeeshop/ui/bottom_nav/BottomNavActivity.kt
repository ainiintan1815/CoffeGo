package com.example.coffeeshop.ui.bottom_nav

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.coffeeshop.R
import com.example.coffeeshop.ui.cart.CartActivity
import com.example.coffeeshop.databinding.ActivityBottomNavBinding
import com.example.coffeeshop.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BottomNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBinding
    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        binding.fab.bringToFront()
        binding.fab.compatElevation = 20f
        binding.navView.bringToFront()



        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> navController.navigate(R.id.homeFragment)
                R.id.profileFragment -> navController.navigate(R.id.profileFragment)
                R.id.notificationFragment -> navController.navigate(R.id.notificationFragment)
                R.id.settingFragment -> navController.navigate(R.id.settingFragment)
            }
            true
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // 🔥 Pasang listener untuk badge notifikasi
        val user = auth.currentUser
        if (user != null) {
            db.child("Notifications")
                .child(user.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var unread = 0
                        for (child in snapshot.children) {
                            val notif = child.getValue(NotificationModel::class.java)
                            if (notif != null && notif.isRead == false) {
                                unread++
                            }
                        }
                        updateNotificationBadge(unread)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

        }
    }

    fun updateNotificationBadge(count: Int) {
        val badge = binding.navView.getOrCreateBadge(R.id.notificationFragment)

        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.clearNumber()
            badge.isVisible = false
        }
    }
}

