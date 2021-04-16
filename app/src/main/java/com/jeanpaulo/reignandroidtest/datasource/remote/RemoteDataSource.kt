package com.jeanpaulo.reignandroidtest.datasource.remote

import com.jeanpaulo.buscador_itunes.datasource.remote.service.IHackerNewsService
import com.jeanpaulo.reignandroidtest.datasource.Constants
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.util.Result

/**
 * Concrete implementation of a data source as a db.
 */
class RemoteDataSource internal constructor(
    private val service: IHackerNewsService
) : IRemoteDataSource {

    override suspend fun getHits(page: Int): Result<List<Hit>> {
        try {
            val response = service.searchByDate(Constants.QUERY, page = page)
            if (response.isSuccessful) {
                val hitList = response.body()!!.hits
                return Result.Success(hitList.map { it.toModel() })
            } else return Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    response.message()
                )
            )
        } catch (e: Exception) {
            throw DataSourceException(
                DataSourceException.Error.UNKNOWN_EXCEPTION,
                e.toString()
            )
        }
    }
}
