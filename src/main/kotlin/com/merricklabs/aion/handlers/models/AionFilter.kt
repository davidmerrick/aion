package com.merricklabs.aion.handlers.models

import com.merricklabs.aion.storage.models.DbAionFilter
import java.util.UUID

/**
 * Consists of a url for a calendar and filters for it.
 */
data class AionFilter(val id: UUID, val titleFilters: FieldFilter) {
    companion object {
        fun create(titleFilters: FieldFilter) = AionFilter(UUID.randomUUID(), titleFilters)
    }
}

fun DbAionFilter.toDomain(): AionFilter {
    return AionFilter(id = this.id!!, titleFilters = this.titleFilters!!)
}

fun CreateFilterPayload.toDomain(): AionFilter {
    return AionFilter(id = UUID.randomUUID(), titleFilters = this.titleFilters)
}