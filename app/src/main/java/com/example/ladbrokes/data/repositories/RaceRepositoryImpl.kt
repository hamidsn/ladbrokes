package com.example.ladbrokes.data.repositories

import com.example.ladbrokes.data.GREYHOUND_CATEGORY_ID
import com.example.ladbrokes.data.HARNESS_CATEGORY_ID
import com.example.ladbrokes.data.HORSE_CATEGORY_ID
import com.example.ladbrokes.data.HTTP_ERROR_MESSAGE
import com.example.ladbrokes.data.IO_ERROR_MESSAGE
import com.example.ladbrokes.data.NUMBER_OF_DISPLAYED_RACES
import com.example.ladbrokes.data.Result
import com.example.ladbrokes.data.source.remote.LadbrokesApi
import com.example.ladbrokes.data.source.remote.dto.toListRaces
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.repositories.RaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RaceRepositoryImpl @Inject constructor(
    private val api: LadbrokesApi
) : RaceRepository {
    override fun getRaces(page: Int): Flow<Result<List<Race>>> = flow {
        emit(Result.Loading())
        try {
            val response = api.getRaces().toListRaces()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(
                Result.Error(
                    message = HTTP_ERROR_MESSAGE,
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Result.Error(
                    message = IO_ERROR_MESSAGE,
                    data = null
                )
            )
        }
    }

    override suspend fun getTimeFilteredRaces(
        selectedSeconds: Long,
        list: List<Race>?
    ): Flow<Result<List<Race>>> = flow {
        val response = if ((list?.size ?: 0) < NUMBER_OF_DISPLAYED_RACES) {
            api.getRaces().toListRaces()
        } else {
            //offline-first
            list?.filter { it.seconds != selectedSeconds }
        }

        emit(Result.Success(response))
    }

    override suspend fun getCategoryFilteredRaces(
        noFilters: Boolean,
        filterList: Array<Boolean>,
        list: List<Race>?
    ): Flow<Result<List<Race>>> = flow {

        val predicates: List<(Race) -> Boolean> = listOf(
            { race: Race ->
                if (!noFilters && race.categoryId == GREYHOUND_CATEGORY_ID) {
                    filterList[0]
                } else true
            },
            { race: Race ->
                if (!noFilters && race.categoryId == HARNESS_CATEGORY_ID) {
                    filterList[1]
                } else true
            },
            { race: Race ->
                if (!noFilters && race.categoryId == HORSE_CATEGORY_ID) {
                    filterList[2]
                } else true
            }
        )

        val response = if (noFilters || (list?.size ?: 0) < NUMBER_OF_DISPLAYED_RACES) {
            api.getRaces().toListRaces().filter { candidate ->
                predicates.all { it(candidate) }
            }
        } else {
            //offline-first
            list?.filter { candidate ->
                predicates.all { it(candidate) }
            }
        }

        emit(Result.Success(response))
    }


}