package com.jeanpaulo.reignandroidtest.datasource

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit

interface Repository {
    fun getPagedHitList(listener: (NetworkState) -> Unit): LiveData<PagedList<Hit>>
    fun deleteHit(hit: Hit): Boolean
    fun refreshHits(): Boolean
}