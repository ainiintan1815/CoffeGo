package com.example.coffeeshop.ui.cart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.adapter.CartAdapter
import com.example.coffeeshop.databinding.ActivityCartBinding
import com.example.coffeeshop.helper.ChangeNumberItemsListener
import com.example.coffeeshop.helper.ManagmentCart
import com.example.coffeeshop.model.NotificationModel
import com.example.coffeeshop.model.TransactionModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference


    lateinit var management: ManagmentCart
    private var tax: Double = 0.0
    private val binding: ActivityCartBinding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        management = ManagmentCart(this)

        calculateCart()
        setVariable()
        initCartList()
    }

    private fun initCartList() {
        with(binding) {
            rvCartView.layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            rvCartView.adapter = CartAdapter(
                management.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )
        }
    }

    private fun setVariable() {
        // Tombol back
        binding.ivBack.setOnClickListener { finish() }

        // 🔗 URL Payment Link Midtrans (SANDBOX)
        val paymentUrl =
            "https://app.sandbox.midtrans.com/payment-links/1763431006123" // ganti kalau kamu buat link baru


        binding.proceedCheckoutBtn.setOnClickListener {
            saveTransactionAndNotification()
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
//            startActivity(intent)
        }
    }

    private fun saveTransactionAndNotification() {

        val user = auth.currentUser ?: return
        val uid = user.uid

        val cartItems = management.getListCart()
        val total = management.getTotalFee()
        val percentTax = 0.02
        val tax = (total * percentTax)
        val delivery = 15.0

        val transactionId = db.child("Transactions").child(uid).push().key ?: return
        val notificationId = db.child("Notifications").child(uid).push().key ?: return

        val transaction = TransactionModel(
            id = transactionId,
            items = cartItems,
            totalAmount = total + tax + delivery,
            tax = tax,
            delivery = delivery,
            status = "COMPLETED",
        )

        // Simpan TRANSAKSI
        db.child("Transactions").child(uid).child(transactionId)
            .setValue(transaction)
            .addOnSuccessListener {

                // Buat NOTIFIKASI dengan transactionId
                val notif = NotificationModel(
                    id = notificationId,
                    title = "Transaksi Berhasil Dibuat",
                    deskripsi = "Item Berhasil Dibeli!",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    transactionId = transactionId    // <-- PERUBAHAN PENTING
                )

                // Simpan NOTIFIKASI
                db.child("Notifications").child(uid).child(notificationId)
                    .setValue(notif)
                    .addOnSuccessListener {
                        management.clearCart()

                        goToMidtrans()
                    }
            }
    }


    private fun goToMidtrans() {
        val paymentUrl = "https://app.sandbox.midtrans.com/payment-links/1763431006123"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl)))
    }




    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15.0
        tax = Math.round((management.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((management.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(management.getTotalFee() * 100) / 100

        with(binding) {
            subTotalPriceTxt.text = "Rp $itemTotal"
            totalTaxPriceTxt.text = "Rp $tax"
            deliveryPriceTxt.text = "Rp $delivery"
            totalPriceTxt.text = "Rp $total"
        }
    }




}
