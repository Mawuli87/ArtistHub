package com.messieyawo.artistshub.models.artistTopTracks


import com.google.gson.annotations.SerializedName

data class ArtistTopTracks(
    @SerializedName("data")
    val ArtisTopTrackdata: List<Data>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)