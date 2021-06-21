package com.example.seq3.data.source.remote.api

import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val label: String
)
