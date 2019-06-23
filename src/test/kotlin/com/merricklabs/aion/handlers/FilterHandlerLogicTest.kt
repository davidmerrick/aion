package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.handlers.logic.FilterLogic
import io.kotlintest.shouldBe
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class FilterHandlerLogicTest : AionIntegrationTestBase() {

    private val filterHandlerLogic by inject<FilterLogic>()
    private val mockContext = Mockito.mock(Context::class.java)
    private val mapper by inject<ObjectMapper>()

    @Test
    private fun `Create a filter`() {
        val payload = mapper.writeValueAsString(mapOf("title_filters" to listOf("/^foo/")))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            headers = mapOf(HttpHeaders.CONTENT_TYPE to AionHeaders.V1)
            httpMethod = HttpMethod.POST.toString()
        }
        val response = filterHandlerLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED
        val jsonNode = mapper.readValue(response.body, JsonNode::class.java)
        jsonNode.has("id") shouldBe true
    }

    @Test
    private fun `Should validate headers`() {
        val payload = mapper.writeValueAsString(mapOf("title_filters" to listOf("/^foo/")))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            headers = emptyMap()
            httpMethod = HttpMethod.POST.toString()
        }
        val response = filterHandlerLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_UNSUPPORTED_MEDIA_TYPE
    }
}