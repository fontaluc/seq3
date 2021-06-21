package com.example.seq3.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seq3.data.model.User
import com.example.seq3.data.model.Liste
import com.example.seq3.data.model.Item

@Database(
    entities = [
        User::class,
        Liste::class,
        Item::class
    ],
    version = 3
)
abstract class ToDoRoomDatabase: RoomDatabase() {
    abstract fun listDao(): ListDao
}