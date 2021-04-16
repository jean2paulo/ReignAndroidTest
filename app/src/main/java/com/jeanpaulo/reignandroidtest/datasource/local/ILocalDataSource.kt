package com.jeanpaulo.reignandroidtest.datasource.local

import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import javax.sql.DataSource

/**
 * Interface to the data layer.
 */
interface ILocalDataSource : IDataSource {

    //C

    suspend fun saveHits(hits: List<Hit>): Result<Boolean>

    //R

    suspend fun getHits(): androidx.paging.DataSource.Factory<Int, Hit>

    //U

    //D
}