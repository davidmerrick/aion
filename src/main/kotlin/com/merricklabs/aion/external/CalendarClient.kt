package com.merricklabs.aion.external

import biweekly.Biweekly
import biweekly.ICalendar
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CalendarClient : KoinComponent {

    private val okHttpClient by inject<OkHttpClient>()

    fun fetchCalendar(url: String): ICalendar {
        log.info("Fetching calendar with url $url")
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()?.let {
            return Biweekly.parse(it.string()).first()
        }

        throw IllegalArgumentException("Calendar url returned no content.")
    }
}