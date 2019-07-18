package com.merricklabs.aion.external

import biweekly.Biweekly
import biweekly.ICalendar
import com.google.common.net.HttpHeaders.CONTENT_TYPE
import com.google.common.net.MediaType.I_CALENDAR_UTF_8
import com.merricklabs.aion.exceptions.InvalidCalendarException
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.URI

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

    @Throws(InvalidCalendarException::class)
    fun validateCalendar(url: String) {
        try {
            URI(url)
        } catch (e: Exception) {
            throw InvalidCalendarException(url, "Unparseable URL.")
        }

        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        val hasCalendarHeaders = response.header(CONTENT_TYPE) == I_CALENDAR_UTF_8.toString()
        if (!hasCalendarHeaders) {
            throw InvalidCalendarException(url, "URL did not return iCal media type.")
        }
    }
}