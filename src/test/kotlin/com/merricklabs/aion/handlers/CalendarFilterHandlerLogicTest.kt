package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.handlers.logic.CalendarFilterHandlerLogic
import io.kotlintest.shouldBe
import org.apache.http.HttpStatus.SC_CREATED
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class CalendarFilterHandlerLogicTest : AionIntegrationTestBase() {

    private val calendarFilterHandlerLogic by inject<CalendarFilterHandlerLogic>()
    private val mockContext = Mockito.mock(Context::class.java)
    private val mapper by inject<ObjectMapper>()

    @Test
    private fun `Test POST`() {
        val payload = mapper.writeValueAsString(mapOf("url" to "webcal://www.meetup.com/ScienceOnTapORWA/events/ical/"))
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            body = payload
            httpMethod = HttpMethod.POST.toString()
        }
        val response = calendarFilterHandlerLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED
    }
}