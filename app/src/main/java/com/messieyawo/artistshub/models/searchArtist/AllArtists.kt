package com.messieyawo.artistshub.models.searchArtist


import com.google.gson.annotations.SerializedName

data class AllArtists(
    @SerializedName("data")
    val searchData: List<SearchData>,
    @SerializedName("total")
    val total: Int
)