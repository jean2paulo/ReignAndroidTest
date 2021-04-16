package com.jeanpaulo.buscador_itunes.datasource

import androidx.paging.DataSource
import kotlinx.coroutines.*
import com.jeanpaulo.reignandroidtest.datasource.IRepository
import com.jeanpaulo.reignandroidtest.datasource.PagedDataSourceFactory
import com.jeanpaulo.reignandroidtest.datasource.local.ILocalDataSource
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.remote.IRemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit

/**
 * Default implementation of [MusicsRepository]. Single entry point for managing musics' data.
 */
class Repository(
    private val remote: IRemoteDataSource,
    private val local: ILocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRepository {

    /*override fun getHits(page: Int): List<Hit> {
        return withContext(ioDispatcher) {
            try {

                try {
                    val response = remote.searchByDate(QUERY, page)
                    if (response.isSuccessful)
                        response.sucessed

                } catch (e: DataSourceException) {
                    when (e.knownNetworkError) {
                        DataSourceException.Error.NO_INTERNET_EXCEPTION -> {
                            local.getHits(page)
                        }
                    }
                } catch (e: Exception) {
                    throw e
                }

                local.getHits(playlist)
                return@withContext List<Hit>(0, Hit(0L))
            } catch (e: Exception) {
                when (e.javaClass) {
                    DataSourceException::class.java -> throw  e
                    else -> throw e
                }
            }
        }
    }*/

    override fun getPagedDataSource(
        isOnline: Boolean,
        listenerNetworkState: (NetworkState) -> Unit
    ): DataSource.Factory<Int, Hit> {
        return runBlocking(ioDispatcher) {

            try {
                // CHECK WITH WE ARE ONLINE OR OFF
                val isOnline: Boolean = true // for test
                // CREATE A CORRESPONDENT DATASOURCE
                val factory: PagedDataSourceFactory = PagedDataSourceFactory(
                    local = local,
                    remote = remote,
                    networkStateUpdate = listenerNetworkState
                )

                factory
            } catch (e: Exception) {
                val dataSourceException =
                    DataSourceException(DataSourceException.Error.UNKNOWN_EXCEPTION, e.message!!)
                throw dataSourceException
            }
        }
    }
}
