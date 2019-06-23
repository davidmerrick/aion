package com.merricklabs.aion.handlers.logic

import biweekly.Biweekly
import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.MediaType.I_CALENDAR_UTF_8
import com.merricklabs.aion.exceptions.CalendarNotFoundException
import com.merricklabs.aion.exceptions.InvalidCalendarException
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.models.CreateCalendarPayload
import com.merricklabs.aion.models.toDomain
import com.merricklabs.aion.storage.CalendarStorage
import mu.KotlinLogging
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.util.UUID

private val log = KotlinLogging.logger {}

class CalendarLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {

    private val storage by inject<CalendarStorage>()
    private val mapper by inject<ObjectMapper>()
    private val calendarClient by inject<CalendarClient>()

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

    private fun handlePost(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        ResourceHelpers.validateContentTypeHeaders(request)

        log.info("Handling POST request")
        val createPayload = try {
            val body = request.body
            mapper.readValue(body, CreateCalendarPayload::class.java)
        } catch (e: IOException) {
            throw InvalidCalendarException()
        }

        val calendar = createPayload.toDomain()
        storage.saveCalendar(calendar)
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_CREATED
            body = mapper.writeValueAsString(calendar)
        }
    }

    private fun handleGet(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        ResourceHelpers.validateAcceptHeaders(request)

        val id = request.pathParameters["id"] ?: throw IllegalArgumentException()
        log.info("Fetching calendar with id $id")

        val responseBody = getBody(id)
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_OK
            body = responseBody
            headers = mapOf(HttpHeaders.CONTENT_TYPE to I_CALENDAR_UTF_8.toString())
        }
    }

    private fun getBody(id: String): String {
        val saved = storage.getCalendar(UUID.fromString(id)) ?: throw CalendarNotFoundException(id)
        val fetched = calendarClient.fetchCalendar(saved.sanitizedUrl())
        return Biweekly.write(fetched).go()
    }
}