package com.example.seq3.data.source.remote.api

import com.google.gson.annotations.SerializedName

data class GetUsersResponse (
    @SerializedName("users")
    val usersReponse: List<UserResponse>
        )
