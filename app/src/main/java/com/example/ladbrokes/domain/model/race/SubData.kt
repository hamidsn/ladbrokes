package com.example.ladbrokes.domain.model.race

data class SubData(
    val advertised_start: AdvertisedStart,
    val category_id: String,
    val meeting_id: String,
    val meeting_name: String,
    val race_form: RaceForm,
    val race_id: String,
    val race_name: String,
    val race_number: Int,
    val venue_country: String,
    val venue_id: String,
    val venue_name: String,
    val venue_state: String
)