package com.jeanpaulo.reignandroidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.datasource.Constants
import com.jeanpaulo.reignandroidtest.datasource.Repository
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit

class HitViewModel(
    private val repository: Repository
) : ViewModel() {

    fun refresh() {
        //TODO This is freezing the view.. fix that
        repository.refreshHits()
        _networkState.postValue(NetworkState.DONE)
    }

    fun init() {
        queryLiveData.postValue(Constants.QUERY)
    }

    fun delete(hit: Hit) {
        repository.deleteHit(hit)
    }

    private val queryLiveData = MutableLiveData<String>()


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

    private var forceUpdate: Boolean = false
    private var _hitList: LiveData<PagedList<Hit>>? =
        Transformations.switchMap<String, PagedList<Hit>>(queryLiveData) {
            repository.getPagedHitList {
                _networkState.postValue(it)
            }
        }
    val hitList: LiveData<PagedList<Hit>>? = _hitList


    val empty: LiveData<Boolean> = Transformations.map(hitList!!) {
        it.isEmpty()
    }
}