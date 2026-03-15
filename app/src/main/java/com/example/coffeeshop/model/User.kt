package com.example.coffeeshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String? = null,
    val email:String? = null,
    val fotoProfil:String? = null,

    ): Parcelable
