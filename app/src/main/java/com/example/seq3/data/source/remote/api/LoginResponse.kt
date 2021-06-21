package com.example.seq3.data.source.remote.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("hash")
    val hash: String
)
