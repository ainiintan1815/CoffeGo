package com.example.coffeeshop.helper

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageUrl(url: String, requireContext: Context) {
    Glide.with(this.context.applicationContext)
        .load(url)
        .centerCrop()
        .into(this)
}