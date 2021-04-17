package com.jeanpaulo.reignandroidtest.datasource.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.jeanpaulo.reignandroidtest.datasource.local.entity.HitEntity


@Dao
interface HitDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHit(hit: HitEntity): kotlin.Long

    //R

    @Query("SELECT * FROM hit WHERE objectId NOT IN (SELECT objectId FROM garbage)  ORDER BY createdAt DESC")
    fun getHits(): DataSource.Factory<Int, HitEntity>

    //U

    @Update
    fun updateHit(hit: HitEntity): Int

    //D

    @Query("DELETE FROM hit")
    fun deleteHits(): kotlin.Int

}