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
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.util.AionHeaders
import com.merricklabs.aion.params.ID_LENGTH
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import org.apache.http.HttpStatus.SC_CREATED
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class CalendarLogicTest : AionIntegrationTestBase() {

    private val logic by inject<CalendarLogic>()
    private val mockContext = Mockito.mock(Context::class.java)
    private val mapper by inject<ObjectMapper>()

    @Test
    private fun `Create a calendar`() {
        val url = "webcal://www.meetup.com/ScienceOnTapORWA/events/ical/"
        val requestPath = "/filters"
        val payload = mapper.writeValueAsString(mapOf("url" to url))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            headers = mapOf(
                    CONTENT_TYPE to AionHeaders.AION_VND,
                    HOST to "localhost"
            )
            httpMethod = HttpMethod.POST.toString()
            requestContext = APIGatewayProxyRequestEvent.ProxyRequestContext().withPath(requestPath)
        }
        val response = logic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED

        // Validate headers
        response.headers.containsKey(LOCATION) shouldBe true
        response.headers[LOCATION] shouldContain requestPath

        // Validate body
        val jsonNode = mapper.readValue(response.body, JsonNode::class.java)
        jsonNode.has("id") shouldBe true
        jsonNode.get("id").textValue().length shouldBe ID_LENGTH
        jsonNode.get("url").textValue() shouldBe url
    }

    @Test(expectedExceptions = [InvalidContentTypeException::class])
    private fun `Should validate headers`() {
        val url = "webcal://www.meetup.com/ScienceOnTapORWA/events/ical/"
        val payload = mapper.writeValueAsString(mapOf("url" to url))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            headers = emptyMap()
            httpMethod = HttpMethod.POST.toString()
        }
        logic.handleRequest(mockRequest, mockContext)
    }
}