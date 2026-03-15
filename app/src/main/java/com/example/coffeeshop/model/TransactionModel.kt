package com.example.coffeeshop.model

data class TransactionModel(
    val id: String = "",
    val items: List<ItemsModel> = emptyList(),
    val totalAmount: Double = 0.0,
    val tax: Double = 0.0,
    val delivery: Double = 0.0,
    val status: String = "COMPLETED",
    val timestamp: Long = System.currentTimeMillis()
)
