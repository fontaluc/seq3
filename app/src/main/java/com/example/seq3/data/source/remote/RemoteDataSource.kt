package com.example.seq3.data.source.remote

import com.example.seq3.data.model.Item
import com.example.seq3.data.model.Liste
import com.example.seq3.data.source.remote.api.ToDoApi
import com.example.seq3.data.source.remote.api.ItemResponse
import com.example.seq3.data.source.remote.api.ListResponse
import com.example.seq3.data.source.remote.api.UserResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit instance class
 */

class RemoteDataSource {

    private val BASE_URL = "http://tomnab.fr/todo-api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ToDoApi::class.java)

    suspend fun login(pseudo: String, pass: String) = service.login(pseudo, pass)

    suspend fun getLists(hash: String, uid: Int): List<Liste> = service.getLists(hash).listsResponse.toLists(uid)

    suspend fun getItems(hash: String, lid: Int): List<Item> = service.getItems(hash, lid).itemsResponse.toItems(lid)

    private suspend fun getUsers(hash: String): List<UserResponse> = service.getUsers(hash).usersReponse

    suspend fun getUserId(hash: String, pseudo: String) : Int {
        return getUsers(hash).first { it.pseudo == pseudo }.id
    }

    suspend fun addItem(hash: String, id: Int, label: String) = service.addItem(hash, id, label)

    private fun List<ListResponse>.toLists(uid: Int) = this.map { listResponse ->
        Liste(
            id = listResponse.id,
            idUser = uid,
            label = listResponse.label
        )
    }

    private fun List<ItemResponse>.toItems(lid: Int) = this.map { itemResponse ->
        Item(
            id = itemResponse.id,
            idList = lid,
            label = itemResponse.label,
            checked = itemResponse.checked
        )
    }

    suspend fun updateItem(hash: String, listId: Int, itemId: Int, checked: Int) = service.updateItem(hash, listId, itemId, checked)
}