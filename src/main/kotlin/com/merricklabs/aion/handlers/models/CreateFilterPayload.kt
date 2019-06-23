package com.merricklabs.aion.handlers.models

import com.fasterxml.jackson.annotation.JsonCreator

data class CreateFilterPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(val titleFilters: FieldFilter)