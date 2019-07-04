package com.merricklabs.aion.resources.models

import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.storage.models.DbAionCalendar

/**
 * Consists of a url for a calendar and filters for it.
 */
data class AionCalendar(val id: EntityId, val url: String) {
    companion object {
        fun create(url: String) = AionCalendar(EntityId.create(), url)
    }

    fun sanitizedUrl() = url.replace("webcal://", "http://")
}

fun DbAionCalendar.toDomain(): AionCalendar {
    return AionCalendar(id = id!!, url = url!!)
}

fun CreateCalendarPayload.toDomain(): AionCalendar {
    return AionCalendar(id = EntityId.create(), url = this.url)
}