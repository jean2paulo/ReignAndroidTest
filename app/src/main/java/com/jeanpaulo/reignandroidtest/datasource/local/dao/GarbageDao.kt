package com.jeanpaulo.reignandroidtest.datasource.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.jeanpaulo.reignandroidtest.datasource.local.entity.GarbageEntity
import com.jeanpaulo.reignandroidtest.datasource.local.entity.HitEntity


@Dao
interface GarbageDao {

    //C

    @Insert
    fun insertGarbage(gabage: GarbageEntity): kotlin.Long

    //R

    //U

    //D
}