package com.example.coffeeshop.ui.notification

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.databinding.ActivityDetailNotificationBinding
import com.example.coffeeshop.model.NotificationModel
import com.example.coffeeshop.model.TransactionModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailNotificationBinding
    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private var notification: NotificationModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notification = intent.getParcelableExtra(EXTRA_NOTIFICATION)

        setupUI()
        markAsRead()
        setupEvent()
        loadTransactionDetail()

    }

    private fun markAsRead() {
        val user = auth.currentUser ?: return
        val uid = user.uid
        val notifId = notification?.id ?: return

        db.child("Notifications")
            .child(uid)
            .child(notifId)
            .child("isRead")
            .setValue(true)
    }

    private fun loadTransactionDetail() {
        val user = auth.currentUser ?: return
        val uid = user.uid

        val transId = notification?.transactionId ?: return

        db.child("Transactions").child(uid).child(transId)
            .get()
            .addOnSuccessListener { snap ->
                val trans = snap.getValue(TransactionModel::class.java) ?: return@addOnSuccessListener

                binding.tvTransTotal.text = "Total: Rp ${trans.totalAmount}"
                binding.tvTransTax.text = "Pajak: Rp ${trans.tax}"
                binding.tvTransDelivery.text = "Delivery: Rp ${trans.delivery}"
                binding.tvTransStatus.text = "Status: ${trans.status}"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat transaksi", Toast.LENGTH_SHORT).show()
            }
    }



    private fun setupUI() {
        notification?.let { notif ->
            binding.tvDetailTitle.text = notif.title
            binding.tvDetailDescription.text = notif.deskripsi
            binding.tvDetailTimestamp.text = convertTimestamp(notif.timestamp)
            binding.tvIdTransaksi.text = "ID Transaksi: ${notif.id}"
        }
    }

    private fun setupEvent() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnDeleteNotification.setOnClickListener {
            deleteNotification()
        }
    }

    private fun deleteNotification() {
        val user = auth.currentUser ?: return
        val uid = user.uid
        val notifId = notification?.id ?: return

        db.child("Notifications")
            .child(uid)
            .child(notifId)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Notifikasi dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menghapus notifikasi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun convertTimestamp(ts: Long): String {
        if (ts == 0L) return "-"
        val sdf = SimpleDateFormat("dd MMM yyyy : HH:mm", Locale.getDefault())
        return sdf.format(Date(ts))
    }

    companion object {
        const val EXTRA_NOTIFICATION = "extra_notification"
    }
}