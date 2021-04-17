package com.jeanpaulo.buscador_itunes.datasource

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.Constants
import com.jeanpaulo.reignandroidtest.datasource.Repository
import com.jeanpaulo.reignandroidtest.datasource.local.LocalDataSource
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.DataSourceBoundary
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import com.jeanpaulo.reignandroidtest.model.Hit
import kotlinx.coroutines.*


/**
 * Default implementation of [MusicsRepository]. Single entry point for managing musics' data.
 */
class RepositoryImpl(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    override fun getPagedHitList(
        listener: (NetworkState) -> Unit
    ): LiveData<PagedList<Hit>> {
        return runBlocking(ioDispatcher) {
            try {
                val factory = local.getHits()
                val boundary = DataSourceBoundary(
                    query = Constants.QUERY,
                    remote,
                    local,
                    ioDispatcher,
                    listener
                )

                val pagedList: LiveData<PagedList<Hit>> = LivePagedListBuilder(
                    factory,
                    PagedList.Config.Builder()
                        .setPageSize(Constants.PAGE_SIZE)
                        .setInitialLoadSizeHint(Constants.PAGE_SIZE * 2)
                        .setEnablePlaceholders(false)
                        .build()
                ).setBoundaryCallback(boundary)
                    .build()

                return@runBlocking pagedList
            } catch (e: Exception) {
                val dataSourceException =
                    DataSourceException(DataSourceException.Error.UNKNOWN_EXCEPTION, e.message!!)
                throw dataSourceException
            }
        }
    }

    override fun deleteHit(hit: Hit): Boolean {
        return runBlocking(ioDispatcher) {
            val result = local.deleteHit(hit)
            return@runBlocking result.isSuccessful
        }
    }

    override fun refreshHits(): Boolean {
        return runBlocking(ioDispatcher) {
            val result: Result<List<Hit>> = remote.getHits(Constants.QUERY, 0)
            if (result.isSuccessful) {
                val hits = (result as Result.Success<List<Hit>>).data
                val resultSave = local.saveHits(hits)
                return@runBlocking resultSave.isSuccessful
            }

            return@runBlocking false
        }
    }

}
