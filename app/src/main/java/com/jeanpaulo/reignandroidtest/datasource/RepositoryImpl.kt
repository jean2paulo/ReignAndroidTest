package com.jeanpaulo.reignandroidtest.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.local.LocalDataSource
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
    private val local: LocalDataSource
) : Repository {

    private lateinit var pagedLiveData: LiveData<PagedList<Hit>>
    override fun getPagedHitList(): LiveData<PagedList<Hit>> = pagedLiveData

    override suspend fun buildPagedList(
        listener: (NetworkState) -> Unit,
        scope: CoroutineScope
    ): Deferred<Result<Boolean>> {
        return withContext(Dispatchers.IO) {
            async {
                val factory = local.getHits()
                val boundary = DataSourceBoundary(
                    query = Constants.QUERY,
                    remote,
                    local,
                    scope,
                    listener,
                )

                pagedLiveData = LivePagedListBuilder(
                    factory,
                    PagedList.Config.Builder()
                        .setPageSize(Constants.PAGE_SIZE)
                        .setInitialLoadSizeHint(Constants.PAGE_SIZE * 2)
                        .setEnablePlaceholders(false)
                        .build()
                ).setBoundaryCallback(boundary)
                    .build()

                return@async Result.Success(true)
            }
        }
    }


    override suspend fun deleteHit(hit: Hit): Job {
        return withContext(Dispatchers.IO) {
            launch {
                val result = local.deleteHit(hit)
                result.isSuccessful
            }
        }
    }

    override suspend fun refreshHits(): Deferred<Result<Boolean>> {
        return withContext(Dispatchers.IO) {
            async {
                val result: Result<List<Hit>> = remote.getHits(Constants.QUERY, 0)

                if (result.isSuccessful) {
                    val hits = (result as Result.Success<List<Hit>>).data
                    val resultSave = local.saveHits(hits)

                    return@async Result.Success(result.isSuccessful && resultSave.isSuccessful)
                } else {
                    return@async result as Result.Error
                }
            }
        }
    }
}
