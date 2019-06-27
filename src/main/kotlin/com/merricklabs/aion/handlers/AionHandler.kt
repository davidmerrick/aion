package com.merricklabs.aion.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject

private val log = KotlinLogging.logger {}

abstract class AionHandler : KoinComponent, RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected abstract fun getLogic(): AionLogic

    init {
        startKoin {
            modules(AionModule)
        }
    }

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val mapper by inject<ObjectMapper>()
        log.trace(mapper.writeValueAsString(request))
        return try {
            getLogic().handleRequest(request, context)
        } catch (e: Exception) {
            ResourceHelpers.exceptionToWebAppResponse(e)
        }
    }
}