package com.merricklabs.aion.handlers.logic

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.common.net.MediaType.I_CALENDAR_UTF_8
import com.merricklabs.aion.exceptions.CalendarNotFoundException
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.storage.AionStorage
import mu.KotlinLogging
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.UUID

private val log = KotlinLogging.logger {}

class CalendarExportHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {

    private val storage by inject<AionStorage>()
    private val calendarClient by inject<CalendarClient>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return try {
            handleGet(request)
        } catch (e: Exception) {
            ResourceHelpers.exceptionToWebAppResponse(e)
        }
    }

    private fun handleGet(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
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
        val saved = storage.getCalendar(UUID.fromString(id)) ?: throw CalendarNotFoundException()
        val fetched = calendarClient.fetchCalendar(saved.url)
        return Biweekly.write(fetched).go()
    }
}

fun ICalendar.copyWithEvents(events: List<VEvent>): ICalendar {
    val newCalendar = ICalendar(this)
    newCalendar.setComponent(VEvent::class.java, null)
    events.forEach { newCalendar.addEvent(it) }
    return newCalendar
}