package com.jeanpaulo.reignandroidtest.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jeanpaulo.reignandroidtest.model.DataSourceDetails.Origin
import com.jeanpaulo.reignandroidtest.model.Hit
import java.util.*

@Entity(
    tableName = "hit",
    indices = [Index(
        value = ["objectId"],
        unique = true
    )]
)
class HitEntity(
    @ColumnInfo(name = "objectId") val objectId: Long,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "storyTitle") val storyTitle: String?,
    @ColumnInfo(name = "createdAt") val createdAt: Date?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "storyUrl") val storyUrl: String?,
    //This id is generate for local database consistence but useless until this moment
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long
) {
    fun toModel(): Hit = Hit(objectId).let {
        it.author = author
        it.createdAt = createdAt
        it.title = title
        it.storyTitle = storyTitle
        it.storyUrl = storyUrl

        it.localId = id
        it.origin = Origin.LOCAL

        it
    }
}