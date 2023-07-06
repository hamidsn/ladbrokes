package com.example.ladbrokes.domain.model.race

import com.google.gson.annotations.SerializedName

data class RaceForm(
    @SerializedName("additional_data")
    val additionalData: String,
    val distance: Int,
    @SerializedName("distance_type")
    val distanceType: DistanceType,
    @SerializedName("distance_type_id")
    val distanceTypeId: String,
    val generated: Int,
    @SerializedName("race_comment")
    val raceComment: String,
    @SerializedName("race_comment_alternative")
    val raceCommentAlternative: String,
    @SerializedName("silk_base_url")
    val silkBaseUrl: String,
    @SerializedName("track_condition")
    val trackCondition: TrackCondition,
    @SerializedName("track_condition_id")
    val trackConditionId: String,
    val weather: Weather,
    @SerializedName("weather_id")
    val weatherId: String
)