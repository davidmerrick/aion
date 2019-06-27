package com.merricklabs.aion.handlers.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.LocationFilter

data class CreateFilterPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(val summaryFilter: FieldFilter?, val locationFilter: LocationFilter?)