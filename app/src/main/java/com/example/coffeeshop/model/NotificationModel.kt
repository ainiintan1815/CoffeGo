package com.example.coffeeshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationModel(
    val id: String = "",
    val title: String = "",
    val deskripsi: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    var transactionId: String = ""
): Parcelable

