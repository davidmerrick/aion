package com.merricklabs.aion.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import org.koin.core.context.startKoin

class FilterHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val filterLogic: FilterLogic

    init {
        startKoin {
            modules(AionModule)
        }
        filterLogic = FilterLogic()
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return try {
            filterLogic.handleRequest(input, context)
        } catch (e: Exception) {
            ResourceHelpers.exceptionToWebAppResponse(e)
        }
    }
}