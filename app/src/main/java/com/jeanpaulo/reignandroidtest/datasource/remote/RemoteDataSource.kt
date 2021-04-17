package com.jeanpaulo.reignandroidtest.datasource.remote

import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.util.Result

/**
 * Interface to the data layer.
 */
interface RemoteDataSource {

    //C

    //R

    suspend fun getHits(query: String, page: Int): Result<List<Hit>>

    //U

    //D
}