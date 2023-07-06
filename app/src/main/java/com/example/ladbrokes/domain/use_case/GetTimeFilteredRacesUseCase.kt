package com.example.ladbrokes.domain.use_case

import com.example.ladbrokes.data.Result
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.repositories.RaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimeFilteredRacesUseCase @Inject constructor(
    private val repository: RaceRepository
) {
    suspend operator fun invoke(
        selectedSeconds: Long,
        list: List<Race>?
    ): Flow<Result<List<Race>>> {
        return repository.getTimeFilteredRaces(selectedSeconds, list)
    }
}