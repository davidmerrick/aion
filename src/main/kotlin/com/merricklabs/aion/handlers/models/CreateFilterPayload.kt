package com.merricklabs.aion.handlers.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.merricklabs.aion.params.FieldFilter

data class CreateFilterPayload @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
constructor(val subjectFilter: FieldFilter)