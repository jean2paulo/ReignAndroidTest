package com.jeanpaulo.reignandroidtest.datasource.remote

import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.util.Result

/**
 * Interface to the data layer.
 */
interface IRemoteDataSource : IDataSource {

    //C

    //R

    suspend fun getHits(page: Int): Result<List<Hit>>

    //U

    //D
}