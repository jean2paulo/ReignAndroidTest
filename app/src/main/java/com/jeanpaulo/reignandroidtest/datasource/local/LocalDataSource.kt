package com.jeanpaulo.reignandroidtest.datasource.local

import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import androidx.paging.DataSource

/**
 * Interface to the data layer.
 */
interface LocalDataSource {

    //C

    suspend fun saveHits(hits: List<Hit>): Result<Boolean>

    //R

    suspend fun getHits(): DataSource.Factory<Int, Hit>

    //U

    //D

    suspend fun deleteHit(hit: Hit): Result<Boolean>
}