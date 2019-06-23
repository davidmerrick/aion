package com.merricklabs.aion.handlers.logic

import biweekly.Biweekly
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.handlers.util.applyFilter
import com.merricklabs.aion.handlers.util.getCalendarId
import com.merricklabs.aion.storage.CalendarStorage
import com.merricklabs.aion.storage.FilterStorage
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class ApplyFilterLogic : AionLogic, KoinComponent {

    private val calendarStorage by inject<CalendarStorage>()
    private val filterStorage by inject<FilterStorage>()
    private val calendarClient by inject<CalendarClient>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return getFilteredCalendar(request)
    }

    private fun getFilteredCalendar(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        log.info("Handling apply filter request")
        ResourceHelpers.validateAcceptHeaders(request)

        val calendarId = request.getCalendarId()
        val filterId = request.getFilterId()
        val filter = filterStorage.getFilter(filterId)
        val calendarUrl = calendarStorage.getCalendar(calendarId).sanitizedUrl()

        val filteredCalendar = calendarClient.fetchCalendar(calendarUrl)
                .applyFilter(filter)
        val returnBody = Biweekly.write(filteredCalendar).go()

        return APIGatewayProxyResponseEvent().apply {
            body = returnBody
            statusCode = HttpStatus.SC_OK
        }
    }
}