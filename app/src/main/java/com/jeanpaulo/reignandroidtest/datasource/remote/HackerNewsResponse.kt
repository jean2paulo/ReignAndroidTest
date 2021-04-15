package com.jeanpaulo.reignandroidtest.datasource.remote

import com.jeanpaulo.reignandroidtest.datasource.remote.json_objects.HitJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HackerNewsResponse(
    @Json(name = "hits") val hits: List<HitJson>,
    @Json(name = "page") val page: Int,
    @Json(name = "hitsPerPage") val hitsPerPage: Int
)
