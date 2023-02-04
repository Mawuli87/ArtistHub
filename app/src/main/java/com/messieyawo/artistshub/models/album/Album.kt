package com.messieyawo.artistshub.models.album


import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("data")
    val AlbumsData: List<AlbumsData>,
    @SerializedName("total")
    val total: Int
)