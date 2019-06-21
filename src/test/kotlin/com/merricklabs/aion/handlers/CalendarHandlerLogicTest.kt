package com.merricklabs.aion.handlers

import com.merricklabs.aion.AionIntegrationTestBase
import org.koin.test.inject
import org.testng.annotations.Test

class CalendarHandlerLogicTest : AionIntegrationTestBase() {

    private val calendarHandlerLogic by inject<CalendarHandlerLogic>()

    @Test
    private fun `Test GET`(){
        val body = calendarHandlerLogic.getBody()
        println(body)
    }
}