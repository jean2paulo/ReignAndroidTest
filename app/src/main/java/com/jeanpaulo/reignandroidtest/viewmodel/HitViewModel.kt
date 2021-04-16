package com.jeanpaulo.reignandroidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.Constants
import com.jeanpaulo.reignandroidtest.datasource.IRepository
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.view.util.Event

class HitViewModel(
    private val repository: IRepository
) : ViewModel() {

    fun refresh() {
        TODO("Not yet implemented")
    }

    fun buildResponseList() {

        _networkState.postValue(NetworkState.LOADING)

        val dataSourceFactory =
            repository.getPagedDataSource(true) { _networkState.postValue(it) }

        _hitList = LivePagedListBuilder(
            dataSourceFactory,
            PagedList.Config.Builder()
                .setPageSize(Constants.PAGE_SIZE)
                .setInitialLoadSizeHint(Constants.PAGE_SIZE * 2)
                .setEnablePlaceholders(false)
                .build()
        ).build()
    }

    private var _hitList: LiveData<PagedList<Hit>>? = MutableLiveData<PagedList<Hit>>()
    val hitList: LiveData<PagedList<Hit>>? = _hitList

    private var _errorLoading: MutableLiveData<DataSourceException?> =
        MutableLiveData()
    val errorLoading: LiveData<DataSourceException?> = _errorLoading

    private var _networkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    val dataLoading: LiveData<Boolean> = Transformations.switchMap<NetworkState, Boolean>(
        _networkState
    ) { state ->
        when (state) {
            NetworkState.LOADING -> {
                MutableLiveData<Boolean>(true)
            }

            NetworkState.DONE -> {
                _errorLoading.postValue(null)
                MutableLiveData<Boolean>(false)
            }

            NetworkState.ERROR -> {
                _errorLoading.postValue(state.exception)
                MutableLiveData<Boolean>(false)
            }
        }
    }

    val empty: LiveData<Boolean> = Transformations.map(hitList!!) {
        it.isEmpty()
    }
}