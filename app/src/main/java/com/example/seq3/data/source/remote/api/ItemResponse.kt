package com.example.seq3.data.source.remote.api

import com.example.seq3.data.model.Item
import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val label: String,
    @SerializedName("checked")
    var checked: Int
) {
    fun toItem(lid: Int): Item = Item(
        id = this.id,
        idList = lid,
        label = this.label,
        checked = this.checked
    )

}