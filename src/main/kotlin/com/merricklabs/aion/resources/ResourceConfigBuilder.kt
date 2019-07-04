package com.merricklabs.aion.resources

import com.merricklabs.aion.resources.mappers.HttpExceptionMapper
import com.merricklabs.aion.resources.mappers.IllegalArgumentExceptionMapper
import org.glassfish.jersey.server.ResourceConfig
import org.koin.core.KoinComponent
import org.koin.core.inject

class ResourceConfigBuilder : KoinComponent {

    private val calendarResource by inject<CalendarResource>()
    private val filterResource by inject<FilterResource>()

    fun build() = ResourceConfig()
            .register(calendarResource)
            .register(filterResource)
            .register(HttpExceptionMapper::class.java)
            .register(IllegalArgumentExceptionMapper::class.java)!!
}