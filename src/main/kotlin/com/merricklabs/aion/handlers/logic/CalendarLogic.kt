package com.merricklabs.aion.handlers.logic

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.models.toDomain
import com.merricklabs.aion.handlers.util.AionHeaders
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.handlers.util.getCalendarId
import com.merricklabs.aion.handlers.util.readBody
import com.merricklabs.aion.storage.CalendarStorage
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject

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
        log.info("Handling POST request")
        ResourceHelpers.validateContentTypeHeaders(request)

        val toCreate = request.readBody(mapper, CreateCalendarPayload::class.java)
                .toDomain()
        storage.saveCalendar(toCreate)
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_CREATED
            body = mapper.writeValueAsString(toCreate)
            headers = mapOf(
                    HttpHeaders.CONTENT_TYPE to AionHeaders.AION_VND,
                    HttpHeaders.LOCATION to "${request.requestContext.resourcePath}/${toCreate.id.value}"
            )
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