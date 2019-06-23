package com.merricklabs.aion.handlers.logic

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.exceptions.InvalidCalendarException
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.models.toDomain
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.storage.CalendarStorage
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.util.UUID

private val log = KotlinLogging.logger {}

class CalendarLogic : AionLogic, KoinComponent {

    private val storage by inject<CalendarStorage>()
    private val mapper by inject<ObjectMapper>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return when (HttpMethod.valueOf(request.httpMethod)) {
            HttpMethod.POST -> createCalendar(request)
            else -> getCalendar(request)
        }
    }

    private fun createCalendar(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
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

    private fun getCalendar(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        ResourceHelpers.validateAcceptHeaders(request)

        val id = request.getCalendarId()
        log.info("Fetching calendar with id $id")

        val calendar = storage.getCalendar(id)
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_OK
            body = mapper.writeValueAsString(calendar)
        }
    }
}

fun APIGatewayProxyRequestEvent.getCalendarId(): UUID {
    val id = this.pathParameters["calendarId"] ?: throw IllegalArgumentException()
    return UUID.fromString(id)
}