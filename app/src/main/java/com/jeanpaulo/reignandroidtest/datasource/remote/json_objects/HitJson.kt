package com.jeanpaulo.reignandroidtest.datasource.remote.json_objects

import com.jeanpaulo.reignandroidtest.model.DataSourceDetails.Origin
import com.jeanpaulo.reignandroidtest.model.Hit
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class HitJson(
    @Json(name = "objectID") val objectId: Long,
    @Json(name = "story-title") val storyTitle: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "author") val author: String?,
    @Json(name = "created_at") val createdAt: Date?
) {
    fun getValidTitle(): String = storyTitle ?: (title ?: "NO TITLE")

    fun toModel(): Hit = Hit(objectId).let {
        it.storyTitle = storyTitle
        it.title = title
        it.author = author
        it.createdAt = createdAt

        it.localId = 0
        it.origin = Origin.REMOTE
        it
    }
}