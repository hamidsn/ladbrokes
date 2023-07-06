package com.example.ladbrokes.domain.use_case

import com.example.ladbrokes.data.Result
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.repositories.RaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryFilteredRacesUseCase @Inject constructor(
    private val repository: RaceRepository
) {
    suspend operator fun invoke(
        noFilters: Boolean,
        filterList: Array<Boolean>,
        list: List<Race>?
    ): Flow<Result<List<Race>>> {
        return repository.getCategoryFilteredRaces(noFilters, filterList, list)
    }
}