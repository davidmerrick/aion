package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.exceptions.InvalidContentTypeException
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.util.AionHeaders
import com.merricklabs.aion.params.ID_LENGTH
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
        val payload = mapper.writeValueAsString(mapOf("url" to url))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            headers = mapOf(HttpHeaders.CONTENT_TYPE to AionHeaders.V1)
            httpMethod = HttpMethod.POST.toString()
        }
        val response = logic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED
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