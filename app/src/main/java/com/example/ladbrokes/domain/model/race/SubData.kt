package com.example.ladbrokes.domain.model.race

import com.google.gson.annotations.SerializedName

data class SubData(
    @SerializedName("advertised_start")
    val advertisedStart: AdvertisedStart,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("meeting_id")
    val meetingId: String,
    @SerializedName("meeting_name")
    val meetingName: String,
    @SerializedName("race_form")
    val raceForm: RaceForm,
    @SerializedName("race_id")
    val raceId: String,
    @SerializedName("race_name")
    val raceName: String,
    @SerializedName("race_number")
    val raceNumber: Int,
    @SerializedName("venue_country")
    val venueCountry: String,
    @SerializedName("venue_id")
    val venueId: String,
    @SerializedName("venue_name")
    val venueName: String,
    @SerializedName("venue_state")
    val venueState: String
)