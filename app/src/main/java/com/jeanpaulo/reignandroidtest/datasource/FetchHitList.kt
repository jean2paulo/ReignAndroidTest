package com.jeanpaulo.reignandroidtest.datasource

import com.jeanpaulo.reignandroidtest.datasource.local.ILocalDataSource
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.remote.IRemoteDataSource
import com.jeanpaulo.reignandroidtest.model.Hit
import kotlinx.coroutines.runBlocking
import com.jeanpaulo.reignandroidtest.datasource.util.Result

class FetchHitList(
    private val remote: IRemoteDataSource,
    private val local: ILocalDataSource,
    private val onlineRequest: Boolean
) {

    fun execute(
        page: Int
    ): Result<List<Hit>> {
        return runBlocking { transform(page) }
    }

    private suspend fun transform(
        page: Int
    ): Result<List<Hit>> {

        if (onlineRequest) {
            val response = remote.getHits(page = page)
            if (response.isSuccessful) {
                val success = response as Result.Success<List<Hit>>
                local.saveHits(success.data)
            }

            return response
        } else {
            //OFFLINE
            return Result.Error(
                DataSourceException(
                    DataSourceException.Error.NO_INTERNET_EXCEPTION,
                    "OFFLINE NOT IMPLEMENTED"
                )
            )
        }
    }
}