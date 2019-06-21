package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.storage.AionStorage
import mu.KotlinLogging
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_NOT_IMPLEMENTED
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CalendarFilterHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {

    private val storage by inject<AionStorage>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return when (HttpMethod.valueOf(request.httpMethod)) {
            HttpMethod.POST -> handlePost(request)
            else -> handleGet(request)
        }
    }

    private fun handleGet(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        // Todo: For now, just returning a dummy filtered calendar
        return APIGatewayProxyResponseEvent().apply {
            statusCode = SC_NOT_IMPLEMENTED
        }
    }

    private fun handlePost(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        log.info("Handling POST request")
        request.queryStringParameters["url"]?.let {
            log.info("Query string url param: $it")

            storage.saveCalendarFilter(it)
            return APIGatewayProxyResponseEvent().apply {
                statusCode = SC_CREATED
            }
        }

        return APIGatewayProxyResponseEvent().apply {
            statusCode = SC_BAD_REQUEST
        }
    }
}