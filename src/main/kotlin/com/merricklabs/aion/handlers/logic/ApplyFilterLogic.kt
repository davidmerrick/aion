package com.merricklabs.aion.handlers.logic

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
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

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return try {
            handleGet(request)
        } catch (e: Exception) {
            ResourceHelpers.exceptionToWebAppResponse(e)
        }
    }

    private fun handleGet(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        ResourceHelpers.validateAcceptHeaders(request)

        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_NOT_IMPLEMENTED
        }
    }
}