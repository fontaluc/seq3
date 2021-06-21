package com.example.seq3.data.source.remote.api

import com.example.seq3.data.source.remote.api.ListResponse
import com.google.gson.annotations.SerializedName

data class GetListsResponse (
    @SerializedName("lists")
    val listsResponse: List<ListResponse>
        )