package com.merricklabs.aion.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.AionModule
import org.koin.core.context.startKoin

class CalendarFilterHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val calendarHandlerImpl: CalendarFilterHandlerLogic

    init {
        startKoin {
            modules(AionModule)
        }
        calendarHandlerImpl = CalendarFilterHandlerLogic()
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return calendarHandlerImpl.handleRequest(input, context)
    }
}