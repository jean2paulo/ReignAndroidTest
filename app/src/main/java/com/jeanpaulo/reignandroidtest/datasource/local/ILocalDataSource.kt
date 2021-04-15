package com.jeanpaulo.reignandroidtest.datasource.local

import androidx.paging.DataSource
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.local.util.Result

/**
 * Interface to the data layer.
 */
interface ILocalDataSource {

    //C

    suspend fun saveHits(hits: List<Hit>): Result<Boolean>

    //R

    suspend fun getHits(page: List<Hit>): DataSource.Factory<Int, Hit>

    //U

    //D
}