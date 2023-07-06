package com.example.ladbrokes.domain.use_case

import com.example.ladbrokes.data.NUMBER_OF_RACES
import com.example.ladbrokes.data.Result
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.repositories.RaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRacesUseCase @Inject constructor(
    private val repository: RaceRepository
) {
    operator fun invoke(): Flow<Result<List<Race>>> {
        return repository.getRaces(NUMBER_OF_RACES)
    }
}