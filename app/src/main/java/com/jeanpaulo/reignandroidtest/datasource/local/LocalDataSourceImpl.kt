package com.jeanpaulo.reignandroidtest.datasource.local

import androidx.paging.DataSource
import com.jeanpaulo.reignandroidtest.datasource.local.dao.GarbageDao
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import com.jeanpaulo.reignandroidtest.datasource.local.dao.HitDao
import com.jeanpaulo.reignandroidtest.datasource.local.entity.GarbageEntity
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.model.Hit
import java.util.concurrent.Executor


/**
 * Concrete implementation of a data source as a db.
 */
class LocalDataSourceImpl internal constructor(
    private val hitDao: HitDao,
    private val garbageDao: GarbageDao
) : LocalDataSource {

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
            return Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }
    }

    override suspend fun getHits(): DataSource.Factory<Int, Hit> {
        return hitDao.getHits().map { it.toModel() }
    }

    override suspend fun deleteHit(hit: Hit): Result<Boolean> {
        try {
            val garbageEntity = GarbageEntity(hit.objectId)
            garbageDao.insertGarbage(garbageEntity)
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }
    }
}
