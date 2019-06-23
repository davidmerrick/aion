package com.merricklabs.aion.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import org.koin.core.context.startKoin

abstract class AionHandler: RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected abstract fun getLogic(): AionLogic

    init {
        startKoin {
            AionModule
        }
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return try {
            getLogic().handleRequest(input, context)
        } catch (e: Exception) {
            ResourceHelpers.exceptionToWebAppResponse(e)
        }
    }
}