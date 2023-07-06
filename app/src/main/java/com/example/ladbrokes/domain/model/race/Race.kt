package com.example.ladbrokes.domain.model.race

data class Race(
    val category_id: String,
    val meeting_name: String,
    val race_number: Int,
    val seconds: Long
)
