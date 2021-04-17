package com.jeanpaulo.reignandroidtest.datasource.local.entity

import androidx.room.*
import com.jeanpaulo.reignandroidtest.model.DataSourceDetails.Origin
import com.jeanpaulo.reignandroidtest.model.Hit
import java.util.*

@Entity(
    tableName = "garbage",
    foreignKeys = [
        ForeignKey(
            entity = HitEntity::class,
            parentColumns = ["objectId"],
            childColumns = ["objectId"]
        )]
)
class GarbageEntity(
    @ColumnInfo(name = "objectId") val objectId: Long?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
)