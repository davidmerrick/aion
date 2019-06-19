package com.merricklabs.aion

import biweekly.Biweekly
import okhttp3.OkHttpClient
import okhttp3.Request
import org.testng.annotations.Test


// For now, just testing out an iCal lib
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

        // Todo: Create a new calendar with the filtered events and return it

        println("foo")
    }
}