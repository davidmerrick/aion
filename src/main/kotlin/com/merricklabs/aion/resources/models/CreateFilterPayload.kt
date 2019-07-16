package com.merricklabs.aion.resources.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.LocationFilter
import com.merricklabs.aion.params.RsvpStatus

data class CreateFilterPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(
        val summaryFilter: FieldFilter?,
        val locationFilter: LocationFilter?,
        val rsvpStatuses: List<RsvpStatus>?,
        val description: String?
)