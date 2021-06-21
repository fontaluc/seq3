package com.example.seq3.data.source.local.database

import androidx.room.*
import com.example.seq3.data.model.Item
import com.example.seq3.data.model.Liste
import com.example.seq3.data.model.User
import com.example.seq3.data.source.remote.api.ListResponse

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdateLists(lists: List<Liste>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdateItems(lists: List<Item>)

    @Query("SELECT * FROM liste WHERE idUser LIKE :uid")
    suspend fun getLists(uid: Int): List<Liste>

    @Query("SELECT * FROM item WHERE idList LIKE :lid")
    suspend fun getItems(lid: Int): List<Item>

    @Query("SELECT * FROM user WHERE pseudo LIKE :pseudo")
    suspend fun getUser(pseudo: String): User

    @Query("SELECT * FROM item WHERE id LIKE :id")
    fun getItem(id: Int): Item

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM item WHERE sync LIKE 1")
    suspend fun getUpdate() : List<Item>
}