package com.example.ladbrokes.domain.model.race

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("icon_uri")
    val iconUri: String,
    val id: String,
    val name: String,
    @SerializedName("short_name")
    val shortName: String
)