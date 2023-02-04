package com.messieyawo.artistshub.models.allArtistAlbumTracks


import com.google.gson.annotations.SerializedName

data class AllArtistAlbumTracks(
    @SerializedName("data")
    val allArtistAlbumData: List<AllArtistAlbumTracksPlayList>,
    @SerializedName("total")
    val total: Int
)