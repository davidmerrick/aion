package com.merricklabs.aion.models

import com.fasterxml.jackson.annotation.JsonCreator

data class CreateFilterPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(val titleFilters: List<String>)