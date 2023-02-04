package com.messieyawo.artistshub.models.allTypeArtists


import com.google.gson.annotations.SerializedName

data class AllTypeOfArtists(
    @SerializedName("data")
    val data: List<AllArtistTypeData>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)