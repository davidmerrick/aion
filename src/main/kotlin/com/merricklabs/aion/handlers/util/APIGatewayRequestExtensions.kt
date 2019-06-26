package com.merricklabs.aion.handlers.util

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.exceptions.InvalidBodyException
import mu.KotlinLogging
import java.io.IOException

private val log = KotlinLogging.logger {}

fun APIGatewayProxyRequestEvent.getCalendarId(): String {
    return this.pathParameters[PathParams.CALENDAR_ID] ?: throw IllegalArgumentException()
}

fun APIGatewayProxyRequestEvent.getFilterId(): String {
    return this.pathParameters[PathParams.FILTER_ID] ?: throw IllegalArgumentException()
}

fun <T> APIGatewayProxyRequestEvent.readBody(mapper: ObjectMapper, clazz: Class<T>): T {
    return try {
        val body = this.body
        mapper.readValue(body, clazz)
    } catch (e: IOException) {
        log.warn("Error deserializing body", e)
        throw InvalidBodyException()
    }
}