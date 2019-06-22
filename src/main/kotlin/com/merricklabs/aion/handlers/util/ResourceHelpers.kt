package com.merricklabs.aion.handlers.util

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.exceptions.CalendarNotFoundException
import mu.KotlinLogging
import org.apache.http.HttpStatus

private val log = KotlinLogging.logger {}

object ResourceHelpers {

    fun exceptionToWebAppResponse(e: Exception): APIGatewayProxyResponseEvent {
        log.warn("Exception thrown while handling request", e)
        return when (e.cause) {
            is CalendarNotFoundException -> APIGatewayProxyResponseEvent().apply {
                statusCode = HttpStatus.SC_NOT_FOUND
            }
            else -> APIGatewayProxyResponseEvent().apply {
                statusCode = HttpStatus.SC_BAD_REQUEST
            }
        }
    }
}