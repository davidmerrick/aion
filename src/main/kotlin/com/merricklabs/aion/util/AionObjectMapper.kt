package com.merricklabs.aion.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.koin.core.KoinComponent

class AionObjectMapper : ObjectMapper(), KoinComponent {
    init {
        this.registerModule(KotlinModule())
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    }
}