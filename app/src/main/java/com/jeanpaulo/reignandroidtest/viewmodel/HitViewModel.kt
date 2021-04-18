package com.jeanpaulo.reignandroidtest.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.datasource.Constants
import com.jeanpaulo.reignandroidtest.datasource.Repository
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.view.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HitViewModel(
    private val repository: Repository
) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()

    private val _openHitEvent = MutableLiveData<Event<Unit>>()
    val openHitEvent: LiveData<Event<Unit>> = _openHitEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var _hitList: LiveData<PagedList<Hit>>? =
        Transformations.switchMap(queryLiveData) {
            repository.getPagedHitList(_networkState::postValue)
        }
    val hitList: LiveData<PagedList<Hit>>? = _hitList

    fun init() {
        queryLiveData.postValue(Constants.QUERY)
    }

    fun openHit(hit: Hit) {
        hit.storyUrl?.let { _openHitEvent.value = Event(Unit) }
    }

    fun delete(hit: Hit) {
        hit.objectId?.let {
            viewModelScope.launch {
                val result = repository.deleteHit(hit)
                if (result)
                    showSnackbarMessage(R.string.hit_removed)
            }
        }
    }

    fun refresh() {
        GlobalScope.launch {
            val result = repository.refreshHits()
            _networkState.postValue(NetworkState.DONE)

            viewModelScope.launch {
                if (!result.isSuccessful)
                    showSnackbarMessage(R.string.error_update_hit)
            }
        }
    }

    private var _networkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    val dataLoading: LiveData<Boolean> = Transformations.switchMap<NetworkState, Boolean>(
        _networkState
    ) { state ->
        when (state) {
            NetworkState.LOADING -> MutableLiveData<Boolean>(true)
            else -> MutableLiveData<Boolean>(false)
        }
    }

    val empty: LiveData<Boolean> = Transformations.map(hitList!!) {
        it.isEmpty()
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }
}