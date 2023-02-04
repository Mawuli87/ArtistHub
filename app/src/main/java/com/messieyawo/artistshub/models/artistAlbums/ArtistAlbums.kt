package com.messieyawo.artistshub.models.artistAlbums


import com.google.gson.annotations.SerializedName

data class ArtistAlbums(
    @SerializedName("data")
    val artistAlbumData: List<artistAlbumData>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)