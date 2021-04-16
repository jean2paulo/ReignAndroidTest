package com.jeanpaulo.reignandroidtest.datasource.remote.util

import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException

enum class NetworkState(var exception: DataSourceException? = null) {
    DONE,
    LOADING,
    ERROR;
}
