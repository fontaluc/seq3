package com.example.seq3.data.source.local

import android.app.Application
import androidx.room.Room
import com.example.seq3.data.model.Item
import com.example.seq3.data.model.Liste
import com.example.seq3.data.source.local.database.ToDoRoomDatabase

class LocalDataSource(application: Application) {

    private val roomDatabase = Room.databaseBuilder(application, ToDoRoomDatabase::class.java, "room-database")
        .fallbackToDestructiveMigration()
        .build()

    private val listDao = roomDatabase.listDao()

    suspend fun saveOrUpdateLists(lists: List<Liste>) = listDao.saveOrUpdateLists(lists)
    suspend fun saveOrUpdateItems(items: List<Item>) = listDao.saveOrUpdateItems(items)
    suspend fun getLists(uid: Int) = listDao.getLists(uid)
    suspend fun getItems(lid: Int) = listDao.getItems(lid)
    suspend fun updateItem(item: Item) = listDao.updateItem(item)
    suspend fun getUpdate() = listDao.getUpdate()

}