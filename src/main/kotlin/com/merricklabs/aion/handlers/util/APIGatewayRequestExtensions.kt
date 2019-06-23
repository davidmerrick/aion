package com.merricklabs.aion.handlers.util

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.exceptions.InvalidBodyException
import mu.KotlinLogging
import java.io.IOException
import java.util.UUID

private val log = KotlinLogging.logger {}

fun APIGatewayProxyRequestEvent.getCalendarId(): UUID {
    val id = this.pathParameters[PathParams.CALENDAR_ID] ?: throw IllegalArgumentException()
    return UUID.fromString(id)
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