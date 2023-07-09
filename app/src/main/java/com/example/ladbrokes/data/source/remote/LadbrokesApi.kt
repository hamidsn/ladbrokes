package com.example.ladbrokes.data.source.remote

import com.example.ladbrokes.data.source.remote.dto.RacesDto
import retrofit2.http.GET

interface LadbrokesApi {

    @GET("rest/v1/racing/?method=nextraces&count=10")
    suspend fun getRaces(): RacesDto

}