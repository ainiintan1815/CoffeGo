package com.example.coffeeshop.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.adapter.NotificationAdapter
import com.example.coffeeshop.databinding.FragmentHomeBinding
import com.example.coffeeshop.databinding.FragmentNotificationBinding
import com.example.coffeeshop.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val notificationAdapter by lazy { NotificationAdapter() }

    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvNotification.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }

        loadNotifications()
    }

    private fun loadNotifications() {

        val user = auth.currentUser ?: return
        val uid = user.uid

        db.child("Notifications")
            .child(uid)                         // 👈 ambil notifikasi khusus user
            .orderByChild("timestamp")          // jika notif ada field timestamp
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = ArrayList<NotificationModel>()

                    for (child in snapshot.children) {
                        child.getValue(NotificationModel::class.java)?.let {
                            list.add(it)
                        }
                    }

                    // Sort supaya terbaru di atas (opsional)
                    list.sortByDescending { it.timestamp }

                    notificationAdapter.submitList(list)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
