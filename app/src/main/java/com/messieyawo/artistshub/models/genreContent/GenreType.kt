package com.messieyawo.artistshub.models.genreContent


import com.google.gson.annotations.SerializedName

data class GenreType(
    @SerializedName("data")
    val GenreTypeData: List<GenreTypeData>
)