package com.merricklabs.aion.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.handlers.logic.FilterHandlerLogic
import org.koin.core.context.startKoin

class FilterHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val calendarHandlerImpl: FilterHandlerLogic

    init {
        startKoin {
            modules(AionModule)
        }
        calendarHandlerImpl = FilterHandlerLogic()
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return calendarHandlerImpl.handleRequest(input, context)
    }
}