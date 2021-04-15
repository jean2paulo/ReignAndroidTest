package com.jeanpaulo.reignandroidtest.datasource.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.jeanpaulo.reignandroidtest.datasource.local.entity.HitEntity


@Dao
interface HitDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHit(hit: HitEntity): kotlin.Long

    //R

    @Query("SELECT * FROM hit WHERE isDeleted = 0 ORDER BY createdAt DESC")
    suspend fun getHits(): DataSource.Factory<Int ,HitEntity>

    //U

    @Update
    suspend fun updateHit(hit: HitEntity): Int

    //D

    @Query("DELETE FROM hit")
    suspend fun deleteHits(): kotlin.Int

}