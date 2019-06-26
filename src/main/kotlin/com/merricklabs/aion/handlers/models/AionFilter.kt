package com.merricklabs.aion.handlers.models

import biweekly.component.VEvent
import com.merricklabs.aion.storage.models.DbAionFilter
import com.merricklabs.aion.util.IdUtil
import java.util.UUID

/**
 * Consists of a url for a calendar and filters for it.
 */
data class AionFilter(val id: String, val subjectFilter: FieldFilter) {
    fun apply(event: VEvent): Boolean {
        this.subjectFilter.include?.let { filters ->
            filters.asSequence().forEach {
                if (!event.summary.value.contains(it)) {
                    return false
                }
            }
        }
        this.subjectFilter.exclude?.let { filters ->
            filters.asSequence().forEach {
                if (event.summary.value.contains(it)) {
                    return false
                }
            }
        }
        return true
    }
}

fun DbAionFilter.toDomain(): AionFilter {
    return AionFilter(id = this.id!!, subjectFilter = this.subjectFilter!!)
}

fun CreateFilterPayload.toDomain(): AionFilter {
    return AionFilter(id = IdUtil.generateId(), subjectFilter = this.subjectFilter)
}