package com.jeanpaulo.reignandroidtest.model

import android.text.format.DateUtils
import com.jeanpaulo.reignandroidtest.datasource.local.entity.HitEntity
import java.text.SimpleDateFormat
import java.util.*


class Hit(
    val objectId: Long
) : DataSourceDetails() {

    var title: String? = null
    var storyTitle: String? = null
    var author: String? = null
    var createdAt: Date? = null
    var storyUrl: String? = null

    fun toEntity(): HitEntity = HitEntity(
        objectId = objectId,
        title = title,
        storyTitle = storyTitle,
        createdAt = createdAt,
        author = author,
        storyUrl = storyUrl,
        id = localId
    )

    val formatedTitle: String
        get() = title ?: (storyTitle ?: "NO TITLE")

    val formatedCreatedAtDate: String
        get() = if (createdAt != null) {
            DateUtils.getRelativeTimeSpanString(
                createdAt!!.time,
                Calendar.getInstance().timeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        } else "-"

    val footerDetail: String
        get() = "$author - $formatedCreatedAtDate"

    override var localId: Long = 0
    override var origin: Origin = Origin.UNDEF
}