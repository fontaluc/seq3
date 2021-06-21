package com.example.seq3.data.source.remote.api

import com.example.seq3.data.source.remote.api.ItemResponse
import com.google.gson.annotations.SerializedName

data class GetItemsResponse(
    @SerializedName("items")
    val itemsResponse: List<ItemResponse>
)