package com.messieyawo.artistshub.models.slider


import com.google.gson.annotations.SerializedName

data class SliderData(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("total")
    val total: Int
)