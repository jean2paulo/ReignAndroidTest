package com.jeanpaulo.reignandroidtest.datasource.local

import androidx.paging.DataSource
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import com.jeanpaulo.reignandroidtest.datasource.local.dao.HitDao
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.model.Hit


/**
 * Concrete implementation of a data source as a db.
 */
class LocalDataSource internal constructor(
    private val hitDao: HitDao
) : ILocalDataSource {

    override suspend fun saveHits(hits: List<Hit>): Result<Boolean> {
        try {
            var flagSuccess: Boolean = true
            hits.map { hit ->
                val id = hitDao.insertHit(hit.toEntity())
                if (id == 0L) flagSuccess = false
                hit
            }
            return Result.Success(flagSuccess)
        } catch (e: Exception) {
            throw DataSourceException(
                DataSourceException.Error.UNKNOWN_EXCEPTION,
                e.toString()
            )
        }
    }

    override suspend fun getHits(): DataSource.Factory<Int, Hit> {
        //return hitDao.getHits().map { it.toModel() }
        throw Exception("Not Implemented")
    }
}
