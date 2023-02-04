package com.messieyawo.artistshub.models.allArtistAlbumTracks


import com.google.gson.annotations.SerializedName

data class AllArtistAlbumTracksPlayList(
    @SerializedName("artist")
    val artist: Artist,
    @SerializedName("disk_number")
    val diskNumber: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Int,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isrc")
    val isrc: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("readable")
    val readable: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("title_version")
    val titleVersion: String,
    @SerializedName("track_position")
    val trackPosition: Int,
    @SerializedName("type")
    val type: String
)