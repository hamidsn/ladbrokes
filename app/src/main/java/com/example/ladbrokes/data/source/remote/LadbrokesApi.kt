package com.example.ladbrokes.data.source.remote

import com.example.ladbrokes.data.source.remote.dto.CharactersDto
import retrofit2.http.GET

interface LadbrokesApi {

    @GET("rest/v1/racing/?method=nextraces&count=10")
    suspend fun getRaces(): CharactersDto

}