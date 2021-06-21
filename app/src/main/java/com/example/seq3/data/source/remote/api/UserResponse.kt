package com.example.seq3.data.source.remote.api

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("pseudo")
    val pseudo: String
)