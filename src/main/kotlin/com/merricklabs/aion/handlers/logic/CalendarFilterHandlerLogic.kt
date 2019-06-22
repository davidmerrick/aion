package com.merricklabs.aion.handlers.logic

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.MediaType
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.models.AionCalendar
import com.merricklabs.aion.storage.AionStorage
import mu.KotlinLogging
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.client.HttpResponseException
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.UUID

private val log = KotlinLogging.logger {}

class CalendarFilterHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {

    private val storage by inject<AionStorage>()
    private val mapper by inject<ObjectMapper>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return try {
            when (HttpMethod.valueOf(request.httpMethod)) {
                HttpMethod.POST -> handlePost(request)
                else -> handleGet(request)
            }
        } catch (e: Exception) {
            ResourceHelpers.exceptionToWebAppResponse(e)
        }
    }

    private fun handleGet(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        log.info("Handling GET request")
        val id = request.pathParameters["id"] ?: throw IllegalArgumentException()
        log.info("Fetching calendar with id $id")

        val calendar = storage.getCalendar(UUID.fromString(id))
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_OK
            body = mapper.writeValueAsString(calendar)
            headers = mapOf(HttpHeaders.CONTENT_TYPE to MediaType.I_CALENDAR_UTF_8.toString())
        }
    }

    private fun handlePost(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        log.info("Handling POST request")
        request.queryStringParameters["url"]?.let {
            val calendar = AionCalendar.create(it)
            storage.saveCalendarFilter(calendar)
            return APIGatewayProxyResponseEvent().apply {
                statusCode = SC_CREATED
                body = mapper.writeValueAsString(calendar)
            }
        }
        throw HttpResponseException(SC_BAD_REQUEST, "Invalid calendar object.")
    }
}