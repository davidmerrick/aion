package com.merricklabs.aion.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.resources.util.EntityIdSerializer
import org.koin.core.KoinComponent

class AionObjectMapper : ObjectMapper(), KoinComponent {
    init {
        this.registerModule(KotlinModule())
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE

        val module = SimpleModule()
        module.addSerializer(EntityIdSerializer(EntityId::class.java))
        this.registerModule(module)
    }
}