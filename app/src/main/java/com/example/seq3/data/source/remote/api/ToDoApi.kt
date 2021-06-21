package com.example.seq3.data.source.remote.api

import retrofit2.Call
import retrofit2.http.*

/**
 * Interface for defining REST request functions
 */

interface ToDoApi{

    @POST("authenticate")
    //@FormUrlEncoded
    suspend fun login(
        @Query("user") pseudo: String,
        @Query("password") pass: String
        ) : LoginResponse

    @GET("lists")
    suspend fun getLists(
        @Header("hash") hash: String
    ) : GetListsResponse

    @GET("lists/{id}/items")
    suspend fun getItems(
        @Header("hash") hash: String,
        @Path("id") id: Int
    ) : GetItemsResponse

    @PUT("lists/{listId}/items/{itemId}")
    suspend fun updateItem(
        @Header("hash") hash: String,
        @Path("listId") listId: Int,
        @Path("itemId") itemId: Int,
        @Query("check") checked: Int
    ) : UpdateItemResponse

    @POST("lists/{id}/items")
    suspend fun addItem(
        @Header("hash") hash: String,
        @Path("id") id: Int,
        @Query("label") label: String
    ) : UpdateItemResponse

    @GET("users")
    suspend fun getUsers(
        @Header("hash") hash: String
    ) : GetUsersResponse

}