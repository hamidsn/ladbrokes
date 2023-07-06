package com.example.ladbrokes.domain.model.race

import com.google.gson.annotations.SerializedName

data class DistanceType(
    val id: String,
    val name: String,
    @SerializedName("short_name")
    val shortName: String
)