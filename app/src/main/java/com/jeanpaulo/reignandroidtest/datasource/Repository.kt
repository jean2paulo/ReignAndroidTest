package com.jeanpaulo.reignandroidtest.datasource

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit
import kotlinx.coroutines.Deferred
import com.jeanpaulo.reignandroidtest.datasource.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


interface Repository {

    fun getPagedHitList(): LiveData<PagedList<Hit>>

    suspend fun buildPagedList(listener: (NetworkState) -> Unit, scope: CoroutineScope): Deferred<Result<Boolean>>
    suspend fun deleteHit(hit: Hit): Job
    suspend fun refreshHits(): Deferred<Result<Boolean>>
}