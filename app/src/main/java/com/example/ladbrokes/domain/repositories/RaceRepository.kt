package com.example.ladbrokes.domain.repositories

import com.example.ladbrokes.data.Result
import com.example.ladbrokes.domain.model.race.Race
import kotlinx.coroutines.flow.Flow

interface RaceRepository {

    fun getRaces(page: Int): Flow<Result<List<Race>>>

    suspend fun getTimeFilteredRaces(
        selectedSeconds: Long,
        list: List<Race>?
    ): Flow<Result<List<Race>>>

    suspend fun getCategoryFilteredRaces(
        noFilters: Boolean,
        filterList: Array<Boolean>,
        list: List<Race>?
    ): Flow<Result<List<Race>>>
}