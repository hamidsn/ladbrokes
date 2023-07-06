package com.example.ladbrokes.ui.race.home

import com.example.ladbrokes.domain.model.race.Race

data class HomeState(
    val characters: List<Race> = emptyList(),
    val isLoading: Boolean = false,
    val filterList: Array<Boolean> = arrayOf(true, true, true),
    val timeStamp: Long = 0L
)
