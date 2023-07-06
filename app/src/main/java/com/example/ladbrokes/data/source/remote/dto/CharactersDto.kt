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
            category_id = entries.value.category_id,
            meeting_name = entries.value.meeting_name,
            race_number = entries.value.race_number,
            seconds = entries.value.advertised_start.seconds.toLong()
        )
    }
    return resultEntries
}