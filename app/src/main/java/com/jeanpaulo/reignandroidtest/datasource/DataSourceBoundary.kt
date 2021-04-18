package com.jeanpaulo.reignandroidtest.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.local.LocalDataSource
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import com.jeanpaulo.reignandroidtest.model.Hit
import kotlinx.coroutines.*

class DataSourceBoundary(
    private val query: String,
    private val remote: RemoteDataSource,
    private val local: LocalDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    private val listener: (NetworkState) -> Unit
) : PagedList.BoundaryCallback<Hit>() {

    private var lastRequestedPage = 1
    private var isRequestInProgress = false

    private suspend fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        val result: Result<List<Hit>> = remote.getHits(query, lastRequestedPage)
        if (result.isSuccessful) {
            val hits = (result as Result.Success<List<Hit>>).data
            val resultSave = local.saveHits(hits)
            if (resultSave.isSuccessful) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        } else isRequestInProgress = false

    }


    override fun onZeroItemsLoaded() {
        GlobalScope.async(ioDispatcher) {
            listener(NetworkState.LOADING)
            requestAndSaveData(query)
            listener(NetworkState.DONE)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Hit) {
        GlobalScope.async(ioDispatcher) { requestAndSaveData(query) }
    }
}