package com.example.ladbrokes.data.repositories

import com.example.ladbrokes.data.Result
import com.example.ladbrokes.data.source.remote.LadbrokesApi
import com.example.ladbrokes.data.source.remote.dto.toListCharacters
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.repositories.RaceRepository
import com.example.ladbrokes.util.GREYHOUND_CATEGORY_ID
import com.example.ladbrokes.util.HARNESS_CATEGORY_ID
import com.example.ladbrokes.util.HORSE_CATEGORY_ID
import com.example.ladbrokes.util.NUMBER_OF_DISPLAYED_RACES
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
            val response = api.getRaces().toListCharacters()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(
                Result.Error(
                    message = "Oops, something went wrong",
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Result.Error(
                    message = "Couldn't reach server, check your internet connection",
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
            api.getRaces().toListCharacters()
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
                if (!noFilters && race.category_id == GREYHOUND_CATEGORY_ID) {
                    filterList[0]
                } else true
            },
            { race: Race ->
                if (!noFilters && race.category_id == HARNESS_CATEGORY_ID) {
                    filterList[1]
                } else true
            },
            { race: Race ->
                if (!noFilters && race.category_id == HORSE_CATEGORY_ID) {
                    filterList[2]
                } else true
            }
        )

        val response = if ((list?.size ?: 0) < NUMBER_OF_DISPLAYED_RACES) {
            api.getRaces().toListCharacters().filter { candidate ->
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