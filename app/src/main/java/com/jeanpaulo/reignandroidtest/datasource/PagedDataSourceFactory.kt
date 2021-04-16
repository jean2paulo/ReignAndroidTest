package com.jeanpaulo.reignandroidtest.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jeanpaulo.reignandroidtest.datasource.local.ILocalDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.IRemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PagedDataSourceFactory(
    val remote: IRemoteDataSource,
    val local: ILocalDataSource,
    private val networkStateUpdate: (NetworkState) -> Unit,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource.Factory<Int, Hit>() {

    val sourceLiveData = MutableLiveData<PagedDataSource>()
    lateinit var latestSource: PagedDataSource

    override fun create(): DataSource<Int, Hit> {
        // * notice: It's important that everytime a DataSource factory
        //           create() is invoked a new DataSource instance is created
        val hitUseCase =
            FetchHitList(remote, local, true)

        latestSource =
            PagedDataSource(
                hitUseCase,
                networkStateUpdate,
                ioDispatcher
            )
        sourceLiveData.postValue(latestSource)
        return latestSource
    }
}