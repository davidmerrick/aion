package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders.CONTENT_TYPE
import com.google.common.net.HttpHeaders.HOST
import com.google.common.net.HttpHeaders.LOCATION
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.exceptions.InvalidContentTypeException
import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.ID_LENGTH
import com.merricklabs.aion.testutil.AionTestData.POWELLS_LAT
import com.merricklabs.aion.testutil.AionTestData.POWELLS_LONG
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
                        "summary_filter" to mapOf(
                                "include" to listOf("foo"), "exclude" to listOf("bar")
                        )
                )
        )
    }

    @Test
    private fun `Create a summary filter`() {
        val requestPath = "/filters"
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = mockPayload
            headers = mapOf(
                    CONTENT_TYPE to AION_VND,
                    HOST to "localhost"
            )
            httpMethod = HttpMethod.POST.toString()
            requestContext = APIGatewayProxyRequestEvent.ProxyRequestContext().withPath(requestPath)
        }
        val response = filterLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED

        // Validate headers
        response.headers.containsKey(LOCATION) shouldBe true
        response.headers[LOCATION] shouldContain requestPath

        // Validate body
        val jsonNode = mapper.readValue(response.body, JsonNode::class.java)
        jsonNode.has("id") shouldBe true
        jsonNode.get("id").textValue().length shouldBe ID_LENGTH
    }

    @Test
    private fun `Create a location filter`() {
        val requestPath = "/filters"
        val payload = mapper.writeValueAsString(
                mapOf(
                        "location_filter" to mapOf(
                                "distance_km" to 10,
                                "latitude" to POWELLS_LAT,
                                "longitude" to POWELLS_LONG
                        )
                )
        )
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            headers = mapOf(
                    CONTENT_TYPE to AION_VND,
                    HOST to "localhost"
            )
            httpMethod = HttpMethod.POST.toString()
            requestContext = APIGatewayProxyRequestEvent.ProxyRequestContext().withPath(requestPath)
        }
        val response = filterLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED

        // Validate headers
        response.headers.containsKey(LOCATION) shouldBe true
        response.headers[LOCATION] shouldContain requestPath

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