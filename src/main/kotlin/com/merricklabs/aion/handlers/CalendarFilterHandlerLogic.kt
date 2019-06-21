package com.merricklabs.aion.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import mu.KotlinLogging
import org.apache.http.HttpStatus

private val log = KotlinLogging.logger {}

class CalendarFilterHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return when (input.httpMethod) {
            in "GET" -> handleGet()
            else -> handleGet()
        }
    }

    private fun handleGet(): APIGatewayProxyResponseEvent {
        // Todo: For now, just returning a dummy filtered calendar
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_NOT_IMPLEMENTED
        }
    }

    private fun handlePost(): APIGatewayProxyResponseEvent {
        // Todo: Implement this
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_NOT_IMPLEMENTED
        }
    }
}