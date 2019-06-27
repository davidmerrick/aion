package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.ProxyRequestContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders.CONTENT_TYPE
import com.google.common.net.HttpHeaders.LOCATION
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.exceptions.InvalidContentTypeException
import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.util.AionHeaders
import com.merricklabs.aion.params.ID_LENGTH
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import org.apache.http.HttpStatus.SC_CREATED
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class FilterLogicTest : AionIntegrationTestBase() {

    private val filterLogic by inject<FilterLogic>()
    private val mockContext = Mockito.mock(Context::class.java)
    private val mapper by inject<ObjectMapper>()

    private val mockPayload by lazy {
        mapper.writeValueAsString(
                mapOf(
                        "subject_filter" to mapOf(
                                "include" to listOf("foo"), "exclude" to listOf("bar")
                        )
                )
        )
    }

    @Test
    private fun `Create a filter`() {
        val resourcePath = "http://localhost/filters"
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = mockPayload
            headers = mapOf(CONTENT_TYPE to AionHeaders.AION_VND)
            httpMethod = HttpMethod.POST.toString()
            requestContext = ProxyRequestContext().withResourcePath(resourcePath)
        }
        val response = filterLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED

        // Validate headers
        response.headers.containsKey(LOCATION) shouldBe true
        response.headers[LOCATION] shouldContain resourcePath

        // Validate body
        val jsonNode = mapper.readValue(response.body, JsonNode::class.java)
        jsonNode.has("id") shouldBe true
        jsonNode.get("id").textValue().length shouldBe ID_LENGTH
    }

    @Test(expectedExceptions = [InvalidContentTypeException::class])
    private fun `Should validate headers`() {
        val mockPayload = mapper.writeValueAsString(mapOf("subject_filter" to mapOf("include" to listOf("foo"), "exclude" to listOf("bar"))))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = mockPayload
            headers = emptyMap()
            httpMethod = HttpMethod.POST.toString()
        }
        filterLogic.handleRequest(mockRequest, mockContext)
    }
}