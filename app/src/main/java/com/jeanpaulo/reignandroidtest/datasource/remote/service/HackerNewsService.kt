package com.jeanpaulo.buscador_itunes.datasource.remote.service

import com.jeanpaulo.reignandroidtest.datasource.remote.HackerNewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to the data layer.
 */
interface HackerNewsService {

    @GET("search_by_date")
    suspend fun searchMusic(
        @Query("query") term: String,
    ): Response<HackerNewsResponse>
}