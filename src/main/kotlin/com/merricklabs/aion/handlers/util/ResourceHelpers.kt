package com.merricklabs.aion.handlers.util

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

private val log = KotlinLogging.logger {}

object ResourceHelpers {

    fun exceptionToWebAppResponse(e: Exception): APIGatewayProxyResponseEvent {
        log.warn("Exception thrown while handling request", e)
        val cause = e.cause
        return when (cause) {
            is HttpResponseException -> APIGatewayProxyResponseEvent().apply {
                statusCode = cause.statusCode
                body = cause.message
            }
            else -> APIGatewayProxyResponseEvent().apply {
                statusCode = HttpStatus.SC_BAD_REQUEST
            }
        }
    }
}