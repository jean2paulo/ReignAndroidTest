package com.jeanpaulo.reignandroidtest.datasource

import androidx.paging.PageKeyedDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import kotlinx.coroutines.*

class PagedDataSource(
    private val hitUseCase: FetchHitList,
    private val networkStateUpdate: (NetworkState) -> Unit,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PageKeyedDataSource<Int, Hit>() {

    //IMPLEMENTS

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Hit>
    ) {
        runBlocking(ioDispatcher) {

            //Not necessary cause it was alredy called on createFactory
            updateState(NetworkState.LOADING)

            val response = hitUseCase.execute(0)
            if (response is Result.Success) {
                callback.onResult(
                    response.data,
                    null,
                    0
                )
                delay(100L)
                updateState(NetworkState.DONE)
            } else {
                val exception = (response as Result.Error).exception

                val errorState = NetworkState.ERROR
                errorState.exception = exception
                updateState(errorState)
            }
        }
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        runBlocking {
            val response = hitUseCase.execute(
                params.key
            )
            if (response is Result.Success) {
                callback.onResult(
                    response.data,
                    params.key + 1
                )
                delay(100L)
            } else {
                //setRetry(Action { loadAfter(params, callback) })
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {}

    //OTHER METHODS
    fun updateState(networkState: NetworkState) {
        networkStateUpdate(networkState)
    }
}
