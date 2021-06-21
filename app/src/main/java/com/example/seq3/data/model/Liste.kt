package com.example.seq3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Liste(
    @PrimaryKey
    val id: Int,
    val idUser: Int,
    val label: String
)
