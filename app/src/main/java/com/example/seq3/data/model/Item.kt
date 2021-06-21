package com.example.seq3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey
    val id: Int,
    val idList: Int,
    val label: String,
    var checked: Int,
    var sync: Int = 0
)
