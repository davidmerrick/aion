package com.merricklabs.aion.resources.models

import io.kotlintest.matchers.string.shouldStartWith
import org.testng.annotations.Test

class AionCalendarTest {

    @Test
    fun `URL prefix should convert from webcal`() {
        val url = "webcal://www.meetup.com/Adventure-Society/events/ical/"
        val calendar = AionCalendar.create(url)
        calendar.sanitizedUrl() shouldStartWith "http://"
    }
}