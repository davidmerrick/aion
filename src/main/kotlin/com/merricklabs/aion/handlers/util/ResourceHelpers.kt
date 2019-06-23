package com.merricklabs.aion.handlers.util

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.exceptions.InvalidAcceptHeadersException
import com.merricklabs.aion.handlers.AionHeaders
import mu.KotlinLogging
import org.apache.http.HttpHeaders
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

    fun validateAcceptHeaders(request: APIGatewayProxyRequestEvent) {
        if (!request.headers.containsKey(HttpHeaders.ACCEPT) || request.headers[HttpHeaders.ACCEPT] != AionHeaders.V1) {
            throw InvalidAcceptHeadersException()
        }
    }

    fun validateContentTypeHeaders(request: APIGatewayProxyRequestEvent) {
        if (!request.headers.containsKey(HttpHeaders.CONTENT_TYPE) || request.headers[HttpHeaders.CONTENT_TYPE] != AionHeaders.V1) {
            throw InvalidAcceptHeadersException()
        }
    }
}