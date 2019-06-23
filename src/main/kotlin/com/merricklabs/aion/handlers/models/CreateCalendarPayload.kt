package com.merricklabs.aion.handlers.models

import com.fasterxml.jackson.annotation.JsonCreator

data class CreateCalendarPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(val url: String)