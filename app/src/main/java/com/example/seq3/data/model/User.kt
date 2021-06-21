package com.example.seq3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Int,
    val pseudo: String,
    val pass: String,
    val hash: String
)
