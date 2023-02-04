package com.messieyawo.artistshub.models.artists


import com.google.gson.annotations.SerializedName

data class TopArtists(
    @SerializedName("data")
    val topArtData: List<TopArtistsData>,
    @SerializedName("total")
    val total: Int
)