package com.merricklabs.aion.handlers.models

import biweekly.component.VEvent
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.LocationFilter
import com.merricklabs.aion.storage.models.DbAionFilter

/**
 * Filters for a calendar.
 */
data class AionFilter(val id: EntityId,
                      val subjectFilter: FieldFilter? = null,
                      val locationFilter: LocationFilter? = null
) {
    /**
     * Applies location and subject filters
     */
    fun apply(event: VEvent, geocoderClient: GeocoderClient): Boolean {
        subjectFilter?.let {
            if (!it.apply(event)) {
                return false
            }
        }

        locationFilter?.let {
            if (!it.apply(event, geocoderClient)) {
                return false
            }
        }

        return true
    }
}

fun DbAionFilter.toDomain(): AionFilter {
    return AionFilter(id = id!!, subjectFilter = subjectFilter, locationFilter = locationFilter)
}

fun CreateFilterPayload.toDomain(): AionFilter {
    return AionFilter(id = EntityId.create(), subjectFilter = subjectFilter, locationFilter = locationFilter)
}