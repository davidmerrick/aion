package com.merricklabs.aion.resources.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.merricklabs.aion.params.EntityId


internal class EntityIdSerializer constructor(t: Class<EntityId>) : StdSerializer<EntityId>(t) {

    override fun serialize(someId: EntityId, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(someId.value)
    }
}