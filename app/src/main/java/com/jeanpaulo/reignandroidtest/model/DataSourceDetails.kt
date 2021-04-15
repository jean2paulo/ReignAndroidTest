package com.jeanpaulo.reignandroidtest.model

abstract class DataSourceDetails {

    abstract var localId: Long
    abstract var isDeleted: Boolean
    abstract var origin: Origin

    enum class Origin {
        UNDEF,
        LOCAL,
        REMOTE
    }
}