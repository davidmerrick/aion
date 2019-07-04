package com.merricklabs.aion.resources.models

import com.fasterxml.jackson.annotation.JsonCreator

data class CreateCalendarPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(val url: String)