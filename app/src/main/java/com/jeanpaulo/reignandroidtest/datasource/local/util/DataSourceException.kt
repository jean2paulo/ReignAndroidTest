package com.jeanpaulo.reignandroidtest.datasource.local.util

class DataSourceException(
    val knownNetworkError: Error,
    val exception: String
) : Exception() {

    enum class Error {
        TIMEOUT_EXCEPTION,
        NO_INTERNET_EXCEPTION,
        NULL_EXCEPTION,
        UNKNOWN_EXCEPTION,
        NOT_FOUND_EXCEPTION,
        INSERT_ENTITY_EXCEPTION
    }

    override fun toString(): String {
        return when (knownNetworkError) {
            Error.TIMEOUT_EXCEPTION -> "TIMEOUT_EXCEPTION -> $exception"
            Error.NO_INTERNET_EXCEPTION -> "NO_INTERNET_EXCEPTION -> $exception"
            Error.NULL_EXCEPTION -> "NULL_EXCEPTION -> $exception"
            Error.UNKNOWN_EXCEPTION -> "UNKNOWN_EXCEPTION -> $exception"
            Error.NOT_FOUND_EXCEPTION -> "NOT_FOUND_EXCEPTION -> $exception"
            Error.INSERT_ENTITY_EXCEPTION -> "INSERT_ENTITY_EXCEPTION -> $exception"
        }
    }
}
