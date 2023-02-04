package com.messieyawo.artistshub.models.artists


import com.google.gson.annotations.SerializedName

data class TopArtistsData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("picture_big")
    val pictureBig: String,
    @SerializedName("picture_medium")
    val pictureMedium: String,
    @SerializedName("picture_small")
    val pictureSmall: String,
    @SerializedName("picture_xl")
    val pictureXl: String,
    @SerializedName("position")
    val position: Int,
    @SerializedName("radio")
    val radio: Boolean,
    @SerializedName("tracklist")
    val tracklist: String,
    @SerializedName("type")
    val type: String
)