package com.example.ladbrokes.domain.model.race

import com.google.gson.annotations.SerializedName

data class Race(
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("meeting_name")
    val meetingName: String,
    @SerializedName("race_number")
    val raceNumber: Int,
    val seconds: Long
)
