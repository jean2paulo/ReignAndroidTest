package com.jeanpaulo.reignandroidtest.datasource

import androidx.paging.DataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit

interface IRepository {
    fun getPagedDataSource(isOnline: Boolean, listener: (NetworkState) -> Unit): DataSource.Factory<Int, Hit>
}