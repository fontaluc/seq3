package com.example.seq3.data.source.remote.api

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("pseudo")
    val pseudo: String,
    @SerializedName("pass")
    val pass: String
    )