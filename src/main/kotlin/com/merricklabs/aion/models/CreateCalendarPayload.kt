package com.merricklabs.aion.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateCalendarPayload @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonProperty("url") val url: String)