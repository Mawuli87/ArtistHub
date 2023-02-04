package com.messieyawo.artistshub.models.genre


import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("data")
    val DataGenre: List<GenreData>
)