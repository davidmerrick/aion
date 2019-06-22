package com.merricklabs.aion.handlers

import com.amazonaws.HttpMethod
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
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

    @Test
    private fun `Test POST`(){
        val mockRequest = APIGatewayProxyRequestEvent().apply {
            queryStringParameters = mapOf("url" to "webcal://www.meetup.com/ScienceOnTapORWA/events/ical/")
            httpMethod = HttpMethod.POST.toString()
        }
        val response = calendarFilterHandlerLogic.handleRequest(mockRequest, mockContext)
        response.statusCode shouldBe SC_CREATED
    }
}