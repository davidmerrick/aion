package com.merricklabs.aion.handlers.logic

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders.CONTENT_TYPE
import com.google.common.net.HttpHeaders.LOCATION
import com.google.common.net.MediaType
import com.merricklabs.aion.handlers.models.CreateFilterPayload
import com.merricklabs.aion.handlers.models.toDomain
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.handlers.util.AionLogic
import com.merricklabs.aion.handlers.util.ResourceHelpers
import com.merricklabs.aion.handlers.util.getFilterId
import com.merricklabs.aion.handlers.util.readBody
import com.merricklabs.aion.storage.FilterStorage
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.apache.http.HttpStatus.SC_CREATED
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class FilterLogic : AionLogic, KoinComponent {

    private val storage by inject<FilterStorage>()
    private val mapper by inject<ObjectMapper>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return when (HttpMethod.valueOf(request.httpMethod)) {
            HttpMethod.POST -> createFilter(request)
            else -> getFilter(request)
        }
    }

    private fun getFilter(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        ResourceHelpers.validateAcceptHeaders(request)

        val id = request.getFilterId()
        val filter = storage.getFilter(id)

        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_OK
            body = mapper.writeValueAsString(filter)
            headers = mapOf(CONTENT_TYPE to MediaType.I_CALENDAR_UTF_8.toString())
        }
    }

    private fun createFilter(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        ResourceHelpers.validateContentTypeHeaders(request)

        log.info("Handling POST request")
        val toCreate = request.readBody(mapper, CreateFilterPayload::class.java)
                .toDomain()
        storage.saveFilter(toCreate)
        return APIGatewayProxyResponseEvent().apply {
            statusCode = SC_CREATED
            body = mapper.writeValueAsString(toCreate)
            headers = mapOf(
                    CONTENT_TYPE to AION_VND,
                    LOCATION to "${request.requestContext.resourcePath}/${toCreate.id.value}"
            )
        }
    }
}