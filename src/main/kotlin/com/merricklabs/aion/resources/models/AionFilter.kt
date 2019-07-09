package com.merricklabs.aion.resources.models

import biweekly.component.VEvent
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.LocationFilter
import com.merricklabs.aion.params.PartStatFilter
import com.merricklabs.aion.storage.models.DbAionFilter

/**
 * Filters for a calendar.
 */
data class AionFilter(val id: EntityId,
                      val summaryFilter: FieldFilter? = null,
                      val locationFilter: LocationFilter? = null,
                      val facebookFilter: PartStatFilter? = null,
                      val description: String? = null
) {
    /**
     * Applies location and subject filters
     */
    fun apply(event: VEvent, geocoderClient: GeocoderClient): Boolean {
        summaryFilter?.let {
            if (!it.apply(event.summary.value)) {
                return false
            }
        }

        locationFilter?.let {
            if (!it.apply(event, geocoderClient)) {
                return false
            }
        }

        facebookFilter?.let {
            return false
        }

        return true
    }
}

fun DbAionFilter.toDomain(): AionFilter {
    return AionFilter(
            id = id!!,
            summaryFilter = summaryFilter,
            locationFilter = locationFilter,
            description = description
    )
}

fun CreateFilterPayload.toDomain(): AionFilter {
    return AionFilter(
            id = EntityId.create(),
            summaryFilter = summaryFilter,
            locationFilter = locationFilter,
            description = description
    )
}