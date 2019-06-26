package com.merricklabs.aion.handlers.models

import com.merricklabs.aion.storage.models.DbAionCalendar
import com.merricklabs.aion.util.IdUtil
import java.util.UUID

/**
 * Consists of a url for a calendar and filters for it.
 */
data class AionCalendar(val id: String, val url: String) {
    companion object {
        fun create(url: String) = AionCalendar(IdUtil.generateId(), url)
    }

    fun sanitizedUrl() = url.replace("webcal://", "http://")
}

fun DbAionCalendar.toDomain(): AionCalendar {
    return AionCalendar(id = this.id!!, url = this.url!!)
}

fun CreateCalendarPayload.toDomain(): AionCalendar {
    return AionCalendar(id = IdUtil.generateId(), url = this.url)
}