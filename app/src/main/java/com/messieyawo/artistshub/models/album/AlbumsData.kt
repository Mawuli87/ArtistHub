package com.messieyawo.artistshub.models.album


import com.google.gson.annotations.SerializedName

data class AlbumsData(
    @SerializedName("artist")
    val artist: Artist,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cover_big")
    val coverBig: String,
    @SerializedName("cover_medium")
    val coverMedium: String,
    @SerializedName("cover_small")
    val coverSmall: String,
    @SerializedName("cover_xl")
    val coverXl: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("position")
    val position: Int,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("tracklist")
    val tracklist: String,
    @SerializedName("type")
    val type: String
)