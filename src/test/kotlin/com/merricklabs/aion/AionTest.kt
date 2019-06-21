package com.merricklabs.aion

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.testng.annotations.Test


class AionTest {

    @Test
    private fun `Parse calendar`() {
        val url = "https://www.meetup.com/ScienceOnTapORWA/events/ical/"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        val bodyString = response.body()!!.string()
        val calendar = Biweekly.parse(bodyString).first()


        val filtered = calendar.events.asSequence()
                .filter { !it.summary.value.toLowerCase().contains("microbiome") }
                .toList()

        val cloned = copyWithEvents(calendar, filtered)
        val calendarString = Biweekly.write(cloned).go()
        println(calendarString)
    }

    fun copyWithEvents(original: ICalendar, events: List<VEvent>): ICalendar {
        val newCalendar = ICalendar(original)
        newCalendar.setComponent(VEvent::class.java, null)
        events.forEach { newCalendar.addEvent(it) }
        return newCalendar
    }
}