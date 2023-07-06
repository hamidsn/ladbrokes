package com.example.ladbrokes.data.source.remote.dto

import com.example.ladbrokes.domain.model.race.Data
import com.example.ladbrokes.domain.model.race.Race

data class CharactersDto(
    val data: Data,
    val message: String,
    val status: Int
)

fun CharactersDto.toListCharacters(): List<Race> {
    val resultEntries = data.race_summaries.map { entries ->
        Race(
            categoryId = entries.value.categoryId,
            meetingName = entries.value.meetingName,
            raceNumber = entries.value.raceNumber,
            seconds = entries.value.advertisedStart.seconds.toLong()
        )
    }
    return resultEntries
}